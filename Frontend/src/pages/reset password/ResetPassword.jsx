import React, { useState } from 'react';
import Button from '../../components/button/Button';
import Input from '../../components/input/Input';
import './ResetPassword.css';
import { Link, useLocation,useNavigate } from 'react-router-dom';
const ResetPassword = () => {
  const [formData, setFormData] = useState({
    password: '',
    confirmPassword: ''
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const location = useLocation();
  const navigate = useNavigate();

  const handleChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
    if (errors[field]) {
      setErrors(prev => ({
        ...prev,
        [field]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.password) {
      newErrors.password = 'Password is required';
    } else if (formData.password.length < 8) {
      newErrors.password = 'Password must be at least 8 characters';
    } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(formData.password)) {
      newErrors.password = 'Password must contain uppercase, lowercase, and number';
    }

    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Please confirm your password';
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleContinueToLoginRedirection = (event) => {
    event.preventDefault();
    navigate('/login');

  }

  const handleSubmit = async (e) => {
    console.log("Here");
    e.preventDefault();
    
    if (!validateForm()) return;
    const {email} = location.state || {};
   const resetObj = {
     "email":email,
     "password":formData.password
   }
    const params = new URLSearchParams({
          email: email
    });

    setLoading(true);

    try {
      // Simulate API call to reset password
         await fetch(`http://localhost:8080/api/v1/authentication/reset-password`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(resetObj)
            });
      console.log('Password reset successful');
      setSuccess(true);
    } catch (err) {
      setErrors({ submit: 'Failed to reset password. Please try again.' });
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <div className="success-state">
            <div className="success-icon">
              <svg width="64" height="64" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM10 17L5 12L6.41 10.59L10 14.17L17.59 6.58L19 8L10 17Z" fill="#10B981"/>
              </svg>
            </div>
            <h2 className="success-title">Password Reset Successful</h2>
            <p className="success-message">
              Your password has been successfully reset.
            </p>
            <div className="success-actions">
              <Button
                variant="primary"
                onClick={(event) => handleContinueToLoginRedirection(event)}
                className="auth-button"
              >
                Continue to Login
              </Button>
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
          <h1 className="auth-title">Reset Password</h1>
          <p className="auth-subtitle">
            Create a new password for your account.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          <Input
            label="New Password"
            type="password"
            value={formData.password}
            onChange={(value) => handleChange('password', value)}
            error={errors.password}
            required={true}
            placeholder="Enter your new password"
            autoComplete="new-password"
          />

          <div className="password-requirements">
            <p className="requirements-title">Password must contain:</p>
            <ul className="requirements-list">
              <li className={formData.password.length >= 8 ? 'met' : ''}>
                At least 8 characters
              </li>
              <li className={/(?=.*[a-z])/.test(formData.password) ? 'met' : ''}>
                One lowercase letter
              </li>
              <li className={/(?=.*[A-Z])/.test(formData.password) ? 'met' : ''}>
                One uppercase letter
              </li>
              <li className={/(?=.*\d)/.test(formData.password) ? 'met' : ''}>
                One number
              </li>
            </ul>
          </div>

          <Input
            label="Confirm New Password"
            type="password"
            value={formData.confirmPassword}
            onChange={(value) => handleChange('confirmPassword', value)}
            error={errors.confirmPassword}
            required={true}
            placeholder="Confirm your new password"
            autoComplete="new-password"
          />

          {errors.submit && (
            <div className="error-banner">
              {errors.submit}
            </div>
          )}

          <Button
            type="submit"
            variant="primary"
            loading={loading}
            className="auth-button"
          >
            Reset Password
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

export default ResetPassword;