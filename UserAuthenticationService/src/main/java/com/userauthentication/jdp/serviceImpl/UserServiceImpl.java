package com.userauthentication.jdp.serviceImpl;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.userauthentication.jdp.beans.*;
import com.userauthentication.jdp.config.SecurityConfig;
import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.repository.UserRepository;
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
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User details are incomplete");
        }

        if (userRepository.findByUserName(user.getUserName()) != null) {
            throw new IllegalArgumentException("UserName Already Exists. Please Choose Another UserName");
        }

        if (userRepository.existsByEmailOrPhoneNum(user.getEmail(), user.getPhoneNum()) || (user.getUserId() != 0L && this.userRepository.existsById(user.getUserId()))) {
            throw new IllegalArgumentException("User already exists with the given email, phone or ID");
        }

        try {
            if (user.getUserId() == 0L) {
                long userSeqId = 0L;
                try {
                    userSeqId = sequenceService.getSequenceByCustomer("USERS");
                } catch (Exception e) {
                    log.warn("Sequence generation failed for USERS Table, using fallback", e);
                }
                if (userSeqId == 0L) {
                    userSeqId = System.currentTimeMillis();
                }
                user.setUserId(userSeqId);
            }

            user.setRole(user.getRole() == null ? "USER" : user.getRole());
            user.setCreatedBy(user.getEmail());
            user.setCreationDate(LocalDateTime.now());
            user.setIsActive(true);
            user.setExpDate(LocalDateTime.now().plusYears(20));
            user.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
            user.setNoOfLoginAttempts(0);
            if (user.getFirstName() == null || user.getFirstName().isBlank()) {
                user.setFirstName(user.getEmail().contains("@") ? user.getEmail().substring(0, user.getEmail().indexOf("@")) : user.getUserName());
            }
            userRepository.save(user);

            try {
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setSenderEmail(user.getEmail());
                emailRequest.setSubject("Registration Acknowledgement");
                emailRequest.setTemplateName("welcome-email");
                emailRequest.setUserName(user.getUserName());
                kafkaTemplate.send(TOPIC, emailRequest.getSenderEmail(), emailRequest);
            } catch (Exception e) {
                log.warn("Could not publish registration email event to Kafka: {}", e.getMessage());
            }

            return "User saved successfully";
        } catch (Exception e) {
            log.error("Exception occurred while saving user details: {}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public String loginUser(String email, String password, HttpServletRequest request) throws Exception {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + email + ". Please Register");
        }
        User user = userOptional.get();
        if (!encoder.passwordEncoder().matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = this.SECURITY_TOKEN_GENERATOR.generateToken(user);
        log.info("User token generated successfully for: {}", email);

        try {
            EmailRequest emailRequest = new EmailRequest();
            Map<String, String> loginDetails = getLoginDetails(request);
            emailRequest.setSenderEmail(user.getEmail());
            emailRequest.setDevice(loginDetails.get("Device"));
            emailRequest.setIpaddress(loginDetails.get("IP"));
            emailRequest.setDateAndTime(LocalDateTime.now());
            emailRequest.setTemplateName("login-detect");
            emailRequest.setUserName(user.getUserName());
            emailRequest.setSubject("Login Acknowledgement");
            kafkaTemplate.send(TOPIC, emailRequest.getSenderEmail(), emailRequest);
        } catch (Exception e) {
            log.warn("Could not publish login detection email event to Kafka: {}", e.getMessage());
        }

        return token;
    }

    public String sendOtp(String email, HttpServletRequest request) throws Exception {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + email + ". Please Register");
        }

        int otp = 100000 + new Random().nextInt(900000);
        otpStore.put(email, new OTP(otp, System.currentTimeMillis()));

        try {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setSenderEmail(email);
            emailRequest.setSubject("OTP for Login");
            emailRequest.setTemplateName("otp-email");
            emailRequest.setOtp(String.valueOf(otp));
            emailRequest.setDateAndTime(LocalDateTime.now());
            emailRequest.setDevice("System");
            emailRequest.setIpaddress("Server");
            emailRequest.setUserName(userOptional.get().getUserName());
            kafkaTemplate.send(TOPIC, emailRequest.getSenderEmail(), emailRequest);
        } catch (Exception e) {
            log.warn("Could not publish OTP email event to Kafka: {}", e.getMessage());
        }

        return "OTP sent successfully to " + email;
    }

    public String verifyOtp(String email, int otp) throws Exception {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null");
        }
        OTP otpData = otpStore.get(email);
        if (otpData == null) {
            throw new IllegalArgumentException("No OTP found for this email. Please request an OTP first.");
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - otpData.getTimeStamp() > TimeUnit.MINUTES.toMillis(2)) {
            otpStore.remove(email);
            throw new IllegalArgumentException("OTP expired. Please request a new one.");
        }

        if (otpData.getOtp() != otp) {
            throw new IllegalArgumentException("Invalid OTP. Please check the code and try again.");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        User user = userOptional.get();
        String token = this.SECURITY_TOKEN_GENERATOR.generateToken(user);
        otpStore.remove(email);
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

    public UserDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return userMapper.toDTO(user);
    }


    @Override
    @Transactional
    public void updateUser(String email, UserUpdate bean) {
        try {
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
        } catch (Exception e) {
            log.error("Exception occurred while updating user details{}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }


    public String buildGoogleAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code" + "&scope=openid%20email%20profile";
    }

    public String handleGoogleLogin(GoogleUserInfo profile) {
        try {
            if (profile == null || profile.getEmail() == null) {
                throw new IllegalArgumentException("Google user profile or email is null");
            }
            log.info("Processing Google login for email: {}", profile.getEmail());

            User user = userRepository.findByEmail(profile.getEmail()).orElseGet(() -> {
                log.info("Registering new user from Google OAuth: {}", profile.getEmail());
                User newUser = new User();
                long userSeqId = 0L;
                try {
                    userSeqId = sequenceService.getSequenceByCustomer("USERS");
                } catch (Exception e) {
                    log.warn("Failed to get sequence for USERS, generating timestamp based id", e);
                }
                if (userSeqId == 0L) {
                    userSeqId = System.currentTimeMillis();
                }
                newUser.setUserId(userSeqId);
                newUser.setEmail(profile.getEmail());
                String displayName = (profile.getName() != null && !profile.getName().isBlank())
                        ? profile.getName()
                        : profile.getEmail().substring(0, profile.getEmail().indexOf("@"));
                newUser.setUserName(displayName);
                newUser.setFirstName(displayName);
                newUser.setRole("USER");
                newUser.setIsActive(true);
                newUser.setCreatedBy(profile.getEmail());
                newUser.setCreationDate(LocalDateTime.now());
                newUser.setExpDate(LocalDateTime.now().plusYears(20));
                newUser.setNoOfLoginAttempts(0);
                newUser.setPassword(encoder.passwordEncoder().encode(java.util.UUID.randomUUID().toString()));
                if (profile.getPicture() != null) {
                    try {
                        newUser.setProfilePhoto(profile.getPicture().getBytes());
                    } catch (Exception e) {
                        log.warn("Could not convert picture bytes", e);
                    }
                }
                return userRepository.save(newUser);
            });

            return SECURITY_TOKEN_GENERATOR.generateToken(user);
        } catch (Exception e) {
            log.error("Error while handling Google login: {}", e.getMessage(), e);
            throw new RuntimeException("Google login failed", e);
        }
    }


    /**
     * Exchange authorization code for an access token
     */
    public Map<String, String> exchangeCodeForToken(String code) {
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
    public GoogleUserInfo fetchUserProfile(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;
        return restTemplate.getForObject(url, GoogleUserInfo.class);
    }

    @Override
    @Transactional
    public void updateProfilePhoto(String email, byte[] photo) throws Exception {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setProfilePhoto(photo);
                userRepository.save(user);
            } else {
                throw new Exception("User not found");
            }
        } catch (Exception e) {
            log.error("Exception occurred while updating profile photo: {}", e.getMessage());
            throw e;
        }
    }
}
