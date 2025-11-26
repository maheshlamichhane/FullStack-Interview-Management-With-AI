import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
// import { useAuth } from '../contexts/AuthContext';
import './OTPVerification.css';
import { useLocation } from 'react-router-dom';

const OTPVerification = () => {

  const location = useLocation();

  const [otp, setOtp] = useState(['', '', '', '', '']);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [resendCooldown, setResendCooldown] = useState(0);
  
  const inputRefs = useRef([]);
  // const { verifyOTP, tempToken } = useAuth();
  const navigate = useNavigate();

  // Redirect if no temp token (direct access)
  // useEffect(() => {
  //   if (!tempToken) {
  //     navigate('/login');
  //   }
  // }, [tempToken, navigate]);

  // Resend OTP cooldown timer
  // useEffect(() => {
  //   if (resendCooldown > 0) {
  //     const timer = setTimeout(() => setResendCooldown(resendCooldown - 1), 1000);
  //     return () => clearTimeout(timer);
  //   }
  // }, [resendCooldown]);

  const handleChange = (index, value) => {
    if (!/^\d?$/.test(value)) return; // Only allow numbers

    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);
    setError('');

    // Auto-focus next input
    if (value && index < 4) {
      inputRefs.current[index + 1].focus();
    }
  };

  const handleKeyDown = (index, e) => {
    if (e.key === 'Backspace' && !otp[index] && index > 0) {
      // Move to previous input on backspace
      inputRefs.current[index - 1].focus();
    }
  };

  const handlePaste = (e) => {
    e.preventDefault();
    const pastedData = e.clipboardData.getData('text');
    const pastedNumbers = pastedData.replace(/\D/g, '').split('').slice(0, 5);
    
    const newOtp = [...otp];
    pastedNumbers.forEach((num, index) => {
      if (index < 5) {
        newOtp[index] = num;
      }
    });
    
    setOtp(newOtp);
    
    // Focus the next empty input or last input
    const nextEmptyIndex = newOtp.findIndex(val => val === '');
    const focusIndex = nextEmptyIndex === -1 ? 4 : nextEmptyIndex;
    inputRefs.current[focusIndex].focus();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const otpString = otp.join('');
    
    if (otpString.length !== 5) {
      setError('Please enter all 5 digits');
      return;
    }

    const {email,type} = location.state || {};

    const otpData = {
      "email":email,
      "otp":otpString,
      "type":type
    }

    console.log("Data",otpData);

    setLoading(true);
    try{
    await fetch('http://localhost:8080/api/v1/otp/verify', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(otpData),
            });
    if(type == "REGISTRATION"){
        navigate('/login')
    }
    else{
      navigate('/reset-password',{
        state:{
          email: email
        }
      })
    }
  
    }
    catch(error){
       setOtp(['', '', '', '', '']);
        setError(result.error);
      console.log("Error",error);
    }
    setLoading(false);
  };

  const handleResendOTP = async () => {
    if (resendCooldown > 0) return;

    try {
      const response = await fetch('/api/auth/resend-otp', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ tempToken }),
      });

      const data = await response.json();

      if (response.ok) {
        setResendCooldown(30); // 30 seconds cooldown
        setError('');
        setOtp(['', '', '', '', '']);
        inputRefs.current[0].focus();
      } else {
        setError(data.error || 'Failed to resend OTP');
      }
    } catch (error) {
      setError('Network error occurred');
    }
  };

  return (
    <div className="otp-container">
      <div className="otp-card">
        <div className="otp-header">
          <div className="otp-icon">
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z" stroke="#007bff" strokeWidth="2"/>
              <path d="M9 12L11 14L15 10" stroke="#007bff" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </div>
          <h1 className="otp-title">OTP Verification</h1>
          <p className="otp-subtitle">
            Please enter OTP to verify your account
          </p>
          <p className="otp-instruction">
            We have sent a 5-digit verification code to your registered email
          </p>
        </div>

        <form onSubmit={handleSubmit} className="otp-form">
          <div className="otp-inputs-container">
            <label className="otp-label">Enter OTP Code</label>
            <div className="otp-inputs">
              {otp.map((digit, index) => (
                <input
                  key={index}
                  ref={(el) => (inputRefs.current[index] = el)}
                  type="text"
                  inputMode="numeric"
                  maxLength="1"
                  value={digit}
                  onChange={(e) => handleChange(index, e.target.value)}
                  onKeyDown={(e) => handleKeyDown(index, e)}
                  onPaste={handlePaste}
                  onFocus={(e) => e.target.select()}
                  className={`otp-input ${error ? 'error' : ''}`}
                  autoFocus={index === 0}
                />
              ))}
            </div>
          </div>

          {error && (
            <div className="error-banner">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading || otp.join('').length !== 5}
            className={`otp-button ${loading ? 'loading' : ''}`}
          >
            {loading ? (
              <>
                <div className="spinner"></div>
                Verifying...
              </>
            ) : (
              'Verify OTP'
            )}
          </button>
        </form>

        <div className="otp-footer">
          <p className="resend-text">
            Didn't receive the code?{' '}
            <button
              type="button"
              onClick={handleResendOTP}
              disabled={resendCooldown > 0}
              className="resend-button"
            >
              {resendCooldown > 0 ? `Resend in ${resendCooldown}s` : 'Resend OTP'}
            </button>
          </p>
          
          <button
            type="button"
            onClick={() => navigate('/login')}
            className="back-to-login"
          >
            ← Back to Login
          </button>
        </div>
      </div>
    </div>
  );
};

export default OTPVerification;