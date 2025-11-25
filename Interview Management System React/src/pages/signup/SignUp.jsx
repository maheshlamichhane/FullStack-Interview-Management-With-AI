import Button from '../../components/button/Button';
import Input from '../../components/input/Input';
import { useState } from 'react';
import { Link,useNavigate } from 'react-router-dom';

import './SignUp.css'; 

const SignUp = () => {

  const navigatate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
    acceptTerms: false
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const handleChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));

    // Clear error when user starts typing
    if (errors[field]) {
      setErrors(prev => ({
        ...prev,
        [field]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.firstName.trim()) {
      newErrors.firstName = 'First name is required';
    }

    if (!formData.lastName.trim()) {
      newErrors.lastName = 'Last name is required';
    }

    if (!formData.email) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Email is invalid';
    }

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

    if (!formData.acceptTerms) {
      newErrors.acceptTerms = 'You must accept the terms and conditions';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;

    setLoading(true);
    const signupData = {
      "email":formData.email,
      "password":formData.password,
      "firstName":formData.firstName,
      "lastName":formData.lastName
    }
    
    // Simulate API call
    try {
         // Send via REST API
            const apiResponse = await fetch('http://localhost:8080/api/v1/authentication/signup', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(signupData),
            });
      console.log('Sign up successful:', formData);
      // Here you would typically handle the registration logic
      navigatate('/otp-verification',{
        state:{
          email: signupData.email
        }
      })

    } catch (error) {
      console.error('Sign up failed:', error);
      setErrors({ submit: 'Registration failed. Please try again.' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1 className="auth-title">Create Account</h1>
          <p className="auth-subtitle">Join us today! Fill in your details to get started.</p>
        </div>

        <form className="auth-form">
          <div className="name-fields">
            <Input
              label="First Name"
              value={formData.firstName}
              onChange={(value) => handleChange('firstName', value)}
              error={errors.firstName}
              required={true}
              placeholder="Enter your first name"
              autoComplete="given-name"
            />
            <Input
              label="Last Name"
              value={formData.lastName}
              onChange={(value) => handleChange('lastName', value)}
              error={errors.lastName}
              required={true}
              placeholder="Enter your last name"
              autoComplete="family-name"
            />
          </div>

          <Input
            label="Email Address"
            type="email"
            value={formData.email}
            onChange={(value) => handleChange('email', value)}
            error={errors.email}
            required={true}
            placeholder="Enter your email"
            autoComplete="email"
          />

          <Input
            label="Password"
            type="password"
            value={formData.password}
            onChange={(value) => handleChange('password', value)}
            error={errors.password}
            required={true}
            placeholder="Create a strong password"
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
            label="Confirm Password"
            type="password"
            value={formData.confirmPassword}
            onChange={(value) => handleChange('confirmPassword', value)}
            error={errors.confirmPassword}
            required={true}
            placeholder="Confirm your password"
            autoComplete="new-password"
          />

          <div className="terms-agreement">
            <label className="checkbox-label">
              <input 
                type="checkbox" 
                checked={formData.acceptTerms}
                onChange={(e) => handleChange('acceptTerms', e.target.checked)}
              />
              <span className="checkmark"></span>
              I agree to the <a href="#terms" className="terms-link">Terms and Conditions</a> and <a href="#privacy" className="terms-link">Privacy Policy</a>
            </label>
            {errors.acceptTerms && (
              <div className="field-error">{errors.acceptTerms}</div>
            )}
          </div>

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
            onClick={(e) => handleSubmit(e)}
          >
            Create Account
          </Button>
        </form>

        <div className="auth-footer">
          <p>
            Already have an account?{' '}
            <Link  to="/login" className="auth-link">
              Sign in
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default SignUp;