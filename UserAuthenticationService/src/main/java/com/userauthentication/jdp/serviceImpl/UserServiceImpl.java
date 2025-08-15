package com.userauthentication.jdp.serviceImpl;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.userauthentication.jdp.beans.OTP;
import com.userauthentication.jdp.beans.UserMapper;
import com.userauthentication.jdp.beans.UserUpdate;
import com.userauthentication.jdp.config.SecurityConfig;
import com.userauthentication.jdp.entity.EmailRequest;
import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.repository.UserRepository;
import com.userauthentication.jdp.service.EmailClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final Map<String, OTP> otpStore = new ConcurrentHashMap<>();
    private final UserRepository userRepository;
    private final SecurityConfig encoder;
    private final SecurityTokenGeneratorImpl SECURITY_TOKEN_GENERATOR;
    private final EmailClient emailClient;
    private final SequenceService sequenceService;
    private final UserMapper userMapper;

    // private final StringRedisTemplate redisTemplate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SecurityConfig passwordEncoder, SecurityTokenGeneratorImpl securityTokenGenerator, EmailClient emailClient, SequenceService sequenceService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.encoder = passwordEncoder;
        SECURITY_TOKEN_GENERATOR = securityTokenGenerator;
        this.emailClient = emailClient;
        this.sequenceService = sequenceService;
        this.userMapper = userMapper;
    }


    @Override
    public String saveUser(User user) throws Exception {
        String status = "";
        EmailRequest emailRequest = new EmailRequest();

        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            status = "User details are incomplete";
            return status;
        }
        if (userRepository.findByUserName(user.getUserName()) != null) {
            status = "UserName Already Exists.Please Choose Another UserName";
            return status;
        }
        if (userRepository.existsByEmailOrPhoneNum(user.getEmail(), user.getPhoneNum()) || this.userRepository.existsById(user.getUserId())) {
            status = "User already exists with the given email, phone or ID";
            return status;
        }
        try {
            if (user.getUserId() == 0L) {
                long userSeqId = sequenceService.getSequenceByCustomer("USERS");
                user.setUserId(userSeqId);
            } else {
                log.error("Sequence generation failed for USERS Table");
            }
            user.setRole(user.getRole() == null ? "USER" : user.getRole());
            user.setCreatedBy(user.getEmail());
            user.setCreationDate(java.time.LocalDateTime.now());
            user.setIsActive(true);
            user.setExpDate(java.time.LocalDateTime.now().plusYears(20));
            user.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
            user.setNoOfLoginAttempts(0);
            user.setFirstName(user.getEmail().substring(0, user.getEmail().indexOf("@")));
            userRepository.save(user);
            status = "User saved successfully";
            emailRequest.setSenderEmail(user.getEmail());
            emailRequest.setSubject("Registration Acknowledgement");
            emailRequest.setTemplateName("welcome-email");
            emailRequest.setUserName(user.getUserName());
            emailClient.sendEmail(emailRequest);
            return status;
        } catch (Exception e) {
            log.error("Exception occurred while saving user details", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public String loginUser(String email, String password, HttpServletRequest request) throws Exception {
        String token = null;
        EmailRequest emailRequest = new EmailRequest();
        try {
            if (email == null || password == null) {
                token = "Email and password must not be null";
                return token;
            }
            User user = userRepository.findByEmail(email);
            if (user == null) {
                token = "User not found. Please Register";
                return token;
            }
            if (user != null && encoder.passwordEncoder().matches(password, user.getPassword())) {
                token = this.SECURITY_TOKEN_GENERATOR.generateToken(user);
                log.info("User token generated and returned");
                Map<String, String> loginDetails = getLoginDetails(request);
                emailRequest.setSenderEmail(user.getEmail());
                emailRequest.setDevice(loginDetails.get("Device"));
                emailRequest.setIpaddress(loginDetails.get("IP"));
                emailRequest.setDateAndTime(java.time.LocalDateTime.now());
                emailRequest.setTemplateName("login-detect");
                emailRequest.setUserName(user.getUserName());
                emailRequest.setSubject("Login Acknowledgement");
                emailClient.sendEmail(emailRequest);
            }
        } catch (Exception e) {
            log.error("Exception occurred while logging in", ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return token;
    }

    public String sendOtp(String email, HttpServletRequest request) throws Exception {
        String status = "";
        EmailRequest emailRequest = new EmailRequest();
        if (email == null || email.isEmpty()) {
            throw new Exception("Email must not be null");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            status = "User not found. Please Register";
            return status;
        }
        int otp = 100000 + new Random().nextInt(900000);
//        redisTemplate.opsForValue().set(email, String.valueOf(otp));
        otpStore.put(email, new OTP(otp, System.currentTimeMillis()));
        String message = "Your OTP for login is: " + otp;
        emailRequest.setSenderEmail(email);
        emailRequest.setSubject("OTP for Login");
        emailRequest.setTemplateName("otp-email");
        emailRequest.setOtp(String.valueOf(otp));
        emailRequest.setDateAndTime(java.time.LocalDateTime.now());
        emailRequest.setDevice("System");
        emailRequest.setIpaddress("Server");
        emailRequest.setUserName("user");
        emailClient.sendEmail(emailRequest);
        status = "OTP sent successfully to " + email;
        return status;
    }


    public String verifyOtp(String email, int otp) throws Exception {
        String token = null;
        User newUser = new User();
//        String storedOtp = redisTemplate.opsForValue().get(email);
        if (email == null || email.isEmpty()) {
            throw new Exception("Email must not be null");
        }
        OTP otpData = otpStore.get(email);
        long currentTime = System.currentTimeMillis();
        if (currentTime - otpData.getTimeStamp() > TimeUnit.MINUTES.toMillis(2)) {
            otpStore.remove(email); // Expired OTP is removed
            token = "OTP expired. Please request a new one.";
            return token;
        }
        try {
            if (otpData == null || otpData.getOtp() == 0) {
                token = "Please try again.Resend OTP ? ";
                return token;
            } else {
                if (otpData.getOtp() != 0 && otpData.getOtp() == otp) {
                    User user = userRepository.findByEmail(email);
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
            log.error("Exception occurred while verifying OTP", ExceptionUtils.getStackTrace(e));
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
    public void deleteByUserId(long userId) throws Exception {
        try {
            if (userId != 0L) {
                userRepository.deleteByUserId(userId);
            }
        } catch (Exception e) {
            log.error("Exception occurred while deleting user", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    @Transactional
    @Modifying
    public void updateUserPassword(long userId, String newPassword) {
        if (userId == 0L || newPassword == null) {
            throw new IllegalArgumentException("User Id and new password must not be null");
        }
        userRepository.updateUserPassword(userId, newPassword);
    }

    public UserDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return userMapper.toDTO(userOptional.get());
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
        User user = userRepository.findByEmail(email);
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
    }
}
