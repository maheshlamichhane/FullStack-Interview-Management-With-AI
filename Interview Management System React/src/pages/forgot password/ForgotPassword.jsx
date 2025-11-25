import { useState } from 'react';
import Button from '../../components/button/Button';
import Input from '../../components/input/Input';
import './ForgotPassword.css';
import { Link,useNavigate } from 'react-router-dom';

const ForgotPassword = () => {

  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [emailSent, setEmailSent] = useState(false);

  const handleResetPageRedirect = () => {
    navigate("/reset-password");
  }


  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!email) {
      setError('Email is required');
      return;
    }
    
    if (!/\S+@\S+\.\S+/.test(email)) {
      setError('Please enter a valid email address');
      return;
    }

    setLoading(true);
    setError('');

    try {
      // Simulate API call to send reset email
      await new Promise(resolve => setTimeout(resolve, 1500));
      console.log('Password reset email sent to:', email);
      setEmailSent(true);
    } catch (err) {
      setError('Failed to send reset email. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (emailSent) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <div className="success-state">
            <div className="success-icon">
              <svg width="64" height="64" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM10 17L5 12L6.41 10.59L10 14.17L17.59 6.58L19 8L10 17Z" fill="#10B981"/>
              </svg>
            </div>
            <h2 className="success-title">Check Your Email</h2>
            <p className="success-message">
              We've sent a password reset link to <strong>{email}</strong>
            </p>
            <p className="success-instructions">
              Please check your email and click the link to reset your password. The link will expire in 1 hour.
            </p>
            <div className="success-actions">
              <Button
                variant="primary"
                onClick={() => setEmailSent(false)}
                className="auth-button"
              >
                Resend Email
              </Button>
              <a href="/login" className="back-to-login">
                ← Back to Login
              </a>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1 className="auth-title">Forgot Password</h1>
          <p className="auth-subtitle">
            Enter your email address and we'll send you a link to reset your password.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          <Input
            label="Email Address"
            type="email"
            value={email}
            onChange={setEmail}
            error={error}
            required={true}
            placeholder="Enter your email address"
            autoComplete="email"
          />

          <Button
            type="submit"
            variant="primary"
            loading={loading}
            className="auth-button"
            onClick={() => handleResetPageRedirect()}
          >
            Send Reset Link
          </Button>
        </form>

        <div className="auth-footer">
          <Link to="/login" className="auth-link">
            ← Back to Login
          </Link>
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;