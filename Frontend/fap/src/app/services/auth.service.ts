import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Using API Gateway port 9000 or falling back to userAuthService 8086. We'll use 8086 directly.
  private baseUrl = 'http://localhost:8086/userAuthService';

  constructor(private http: HttpClient) {}

  login(user: any): Observable<string> {
    return this.http.post(`${this.baseUrl}/login`, user, { responseType: 'text' });
  }

  sendOtp(email: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/send-otp/${email}`, {}, { responseType: 'text' });
  }

  verifyOtp(email: string, otp: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/verify-otp/${email}/${otp}`, {}, { responseType: 'text' });
  }

  register(user: any): Observable<string> {
    return this.http.post(`${this.baseUrl}/registerUser`, user, { responseType: 'text' });
  }
}
