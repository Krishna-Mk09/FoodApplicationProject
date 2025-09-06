package com.userauthentication.jdp.serviceImpl;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.userauthentication.jdp.beans.GoogleUserInfo;
import com.userauthentication.jdp.beans.OTP;
import com.userauthentication.jdp.beans.UserMapper;
import com.userauthentication.jdp.beans.UserUpdate;
import com.userauthentication.jdp.config.SecurityConfig;
import com.userauthentication.jdp.beans.EmailRequest;
import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.repository.UserRepository;
import com.userauthentication.jdp.service.EmailClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String TOPIC = "email";
    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;
    private final Map<String, OTP> otpStore = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final SecurityConfig encoder;
    private final SecurityTokenGeneratorImpl SECURITY_TOKEN_GENERATOR;
    private final EmailClient emailClient;
    private final SequenceService sequenceService;
    private final UserMapper userMapper;
    private final RestTemplate restTemplate;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Override
    public String saveUser(User user) throws Exception {
        EmailRequest emailRequest = new EmailRequest();

        if (user == null || user.getEmail() == null || user.getPassword() == null) return "User details are incomplete";

        if (userRepository.findByUserName(user.getUserName()) != null)
            return "UserName Already Exists.Please Choose Another UserName";

        if (userRepository.existsByEmailOrPhoneNum(user.getEmail(), user.getPhoneNum()) || this.userRepository.existsById(user.getUserId()))
            return "User already exists with the given email, phone or ID";

        try {
            if (user.getUserId() == 0L) {
                long userSeqId = sequenceService.getSequenceByCustomer("USERS");
                user.setUserId(userSeqId);
            } else log.error("Sequence generation failed for USERS Table");

            user.setRole(user.getRole() == null ? "USER" : user.getRole());
            user.setCreatedBy(user.getEmail());
            user.setCreationDate(LocalDateTime.now());
            user.setIsActive(true);
            user.setExpDate(LocalDateTime.now().plusYears(20));
            user.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
            user.setNoOfLoginAttempts(0);
            user.setFirstName(user.getEmail().substring(0, user.getEmail().indexOf("@")));
            userRepository.save(user);
            emailRequest.setSenderEmail(user.getEmail());
            emailRequest.setSubject("Registration Acknowledgement");
            emailRequest.setTemplateName("welcome-email");
            emailRequest.setUserName(user.getUserName());
            kafkaTemplate.send(TOPIC, emailRequest.getSenderEmail(), emailRequest);
//            emailClient.sendEmail(emailRequest);
            return "User saved successfully";
        } catch (Exception e) {
            log.error("Exception occurred while  saving user details{} ", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public String loginUser(String email, String password, HttpServletRequest request) throws Exception {
        String token = null;
        EmailRequest emailRequest = new EmailRequest();
        try {
            if (email == null || password == null) {
                return "Email and password must not be null";
            }
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                return "User not found. Please Register";
            }
            User user = userOptional.get();
            if (encoder.passwordEncoder().matches(password, user.getPassword())) {
                token = this.SECURITY_TOKEN_GENERATOR.generateToken(user);
                log.info("User token generated and returned");
                Map<String, String> loginDetails = getLoginDetails(request);
                emailRequest.setSenderEmail(user.getEmail());
                emailRequest.setDevice(loginDetails.get("Device"));
                emailRequest.setIpaddress(loginDetails.get("IP"));
                emailRequest.setDateAndTime(LocalDateTime.now());
                emailRequest.setTemplateName("login-detect");
                emailRequest.setUserName(user.getUserName());
                emailRequest.setSubject("Login Acknowledgement");
               kafkaTemplate.send(TOPIC, emailRequest.getSenderEmail(), emailRequest);
              // emailClient.sendEmail(emailRequest);
            }
        } catch (Exception e) {
            log.error("Exception occurred while logging in{}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return token;
    }

    public String sendOtp(String email, HttpServletRequest request) throws Exception {
        EmailRequest emailRequest = new EmailRequest();
        if (email == null || email.isEmpty()) {
            throw new Exception("Email must not be null");
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) return "User not found. Please Register";

        int otp = 100000 + new Random().nextInt(900000);
        otpStore.put(email, new OTP(otp, System.currentTimeMillis()));
        emailRequest.setSenderEmail(email);
        emailRequest.setSubject("OTP for Login");
        emailRequest.setTemplateName("otp-email");
        emailRequest.setOtp(String.valueOf(otp));
        emailRequest.setDateAndTime(LocalDateTime.now());
        emailRequest.setDevice("System");
        emailRequest.setIpaddress("Server");
        emailRequest.setUserName("user");
        kafkaTemplate.send(TOPIC, emailRequest.getSenderEmail(), emailRequest);
       // emailClient.sendEmail(emailRequest);
        return "OTP sent successfully to " + email;
    }

    public String verifyOtp(String email, int otp) throws Exception {
        String token = null;
        User newUser = new User();
        if (email == null || email.isEmpty()) throw new Exception("Email must not be null");
        OTP otpData = otpStore.get(email);
        long currentTime = System.currentTimeMillis();
        if (currentTime - otpData.getTimeStamp() > TimeUnit.MINUTES.toMillis(2)) {
            otpStore.remove(email);
            return "OTP expired. Please request a new one.";
        }
        try {
            if (otpData.getOtp() == 0) {
                return "Please try again.Resend OTP ? ";
            } else {
                if (otpData.getOtp() == otp) {
                    Optional<User> userOptional = userRepository.findByEmail(email);
                    User user = userOptional.get();
                    newUser.setEmail(email);
                    newUser.setUserId(user.getUserId());
                    newUser.setUserName(user.getUserName());
                    newUser.setPhoneNum(user.getPhoneNum());
                    newUser.setPassword(user.getPassword());
                    newUser.setRole(user.getRole());
                    newUser.setFirstName(user.getFirstName());
                    newUser.setIsActive(user.getIsActive());
                    token = this.SECURITY_TOKEN_GENERATOR.generateToken(newUser);
                    otpStore.remove(email);
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while verifying OTP{}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return token;
    }

    public Map<String, String> getLoginDetails(HttpServletRequest request) {
        Map<String, String> loginDetails = new HashMap<>();
        String ip = request.getHeader("X-Forwarded-For");
        ip = (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) ? request.getRemoteAddr() : ip.split(",")[0];
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        loginDetails.put("IP", ip);
        loginDetails.put("Device", request.getHeader("User-Agent"));
        return loginDetails;
    }


    @Override
    @Transactional
    @Modifying
    public void deleteByUserId(long userId) {
        try {
            if (userId != 0L) userRepository.deleteByUserId(userId);
        } catch (Exception e) {
            log.error("Exception occurred while deleting user{}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    @Transactional
    @Modifying
    public void updateUserPassword(long userId, String newPassword) {
        if (userId == 0L || newPassword == null)
            throw new IllegalArgumentException("User Id and new password must not be null");
        userRepository.updateUserPassword(userId, newPassword);
    }

    public UserDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public void updateUserRole(long userId, String role) {
        userRepository.updateUserRole(userId, role);
    }

    @Override
    public void updateUser(User user) {
    }

    @Override
    @Transactional
    public void updateUser(String email, UserUpdate bean) {
        try{

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.get();
        user.setSecondaryEmail(bean.getUserBusinessEmail());
        user.setAadhaarNumber(bean.getAadhaarNumber());
        user.setLicenseNumber(bean.getLicenseNumber());
        user.setPanNumber(bean.getPanNumber());
        user.setRestaurantPhoto(bean.getRestaurantPhoto());
        user.setRole("OWNER");
        user.setNameAsInLicense(bean.getNameAsInLicense());
        user.setUserName(bean.getUserName());
        user.setGstNumber(bean.getGstNumber());
        userRepository.save(user);
        }catch (Exception e){
            log.error("Exception occurred while updating user details{}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }


    public String buildGoogleAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code" + "&scope=openid%20email%20profile";
    }

    public String handleGoogleLogin(String code) {
        try {
            Map<String, String> tokenResponse = exchangeCodeForToken(code);
            GoogleUserInfo profile = fetchUserProfile(tokenResponse.get("access_token"));
            User user = userRepository.findByEmail(profile.getEmail()).orElseGet(() -> userRepository.save(new User(profile.getEmail(), profile.getName(), profile.getPicture().getBytes())));
            return SECURITY_TOKEN_GENERATOR.generateToken(user);
        } catch (Exception e) {
            log.error("Error while handling Google login: {}", e.getMessage(), e);
            throw new RuntimeException("Google login failed", e);
        }
    }

    /**
     * Exchange authorization code for an access token
     */
    private Map<String, String> exchangeCodeForToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            log.info("Token response: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Token exchange failed: {}", e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token exchange", e);
            throw new RuntimeException("Unexpected error during token exchange", e);
        }
    }

    /**
     * Fetch user profile from Google APIs using an access token
     */
    private GoogleUserInfo fetchUserProfile(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;
        return restTemplate.getForObject(url, GoogleUserInfo.class);
    }
}
