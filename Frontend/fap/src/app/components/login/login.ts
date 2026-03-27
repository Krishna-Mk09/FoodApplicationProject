import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent implements OnInit {

  isActive = false;
  formTitle = 'Sign In';

  // form models
  fullName = '';
  email = '';
  password = '';
  otp = '';

  // state controls
  showName = false;
  showPassword = false;
  showOtpField = false;
  showLogin = false;
  showVerifyOtp = false;
  showSignup = false;
  showSendOtp = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (params['token']) {
        localStorage.setItem('token', params['token']);
        this.router.navigate(['/dashboard']);
      }
    });
  }

  toggle() {
    this.isActive = !this.isActive;
  }

  /* PASSWORD LOGIN */
  openPassword() {
    this.toggle();
    this.resetState();

    this.formTitle = 'Sign In';
    this.showPassword = true;
    this.showLogin = true;
  }

  /* OTP LOGIN */
  openOtp() {
    this.toggle();
    this.resetState();

    this.formTitle = 'Login with OTP';
    this.showSendOtp = true;
  }

  loginWithGoogle() {
    window.location.href = 'http://localhost:8086/userAuthService/google/login';
  }

  /* SHOW OTP */
  showOtp() {
    if (!this.email) {
      alert("Please enter an email");
      return;
    }
    this.authService.sendOtp(this.email).subscribe({
      next: (res) => {
        alert(res || "OTP Sent Successfully!");
        this.showOtpField = true;
        this.showVerifyOtp = true;
      },
      error: (err) => {
        alert("Failed to send OTP. " + (err.error?.message || err.message));
      }
    });
  }

  /* SIGNUP */
  openSignup() {
    this.resetState();

    this.formTitle = 'Create Account';
    this.showName = true;
    this.showPassword = true;
    this.showSignup = true;
  }

  /* SUBMIT LOGIN */
  login() {
    if (!this.email || !this.password) {
      alert("Please enter email and password");
      return;
    }
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (res) => {
        localStorage.setItem('token', res);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        alert("Login Failed. " + (err.error?.message || err.message));
      }
    });
  }

  /* VERIFY OTP */
  verifyOtp() {
    if (!this.email || !this.otp) {
      alert("Please enter email and OTP");
      return;
    }
    this.authService.verifyOtp(this.email, this.otp).subscribe({
      next: (res) => {
        localStorage.setItem('token', res);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        alert("Verification Failed. " + (err.error?.message || err.message));
      }
    });
  }

  /* SUBMIT SIGNUP */
  signup() {
    if (!this.email || !this.password || !this.fullName) {
      alert("Please fill all fields");
      return;
    }
    const user = {
      userName: this.fullName,
      email: this.email,
      password: this.password,
      role: 'USER' // default role
    };
    this.authService.register(user).subscribe({
      next: (res) => {
        alert("Registration Successful! " + res);
        this.openPassword(); // redirect to password login
      },
      error: (err) => {
        alert("Registration Failed. " + (err.error?.message || err.message));
      }
    });
  }

  /* RESET */
  resetState() {
    this.showName = false;
    this.showPassword = false;
    this.showOtpField = false;
    this.showLogin = false;
    this.showVerifyOtp = false;
    this.showSignup = false;
    this.showSendOtp = false;

    // Also reset form models
    this.fullName = '';
    this.email = '';
    this.password = '';
    this.otp = '';
  }
}
