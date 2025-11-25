import { useState } from 'react';

const DatePicker = ({ 
  label, 
  value, 
  onChange, 
  error, 
  minDate, 
  required = false,
  showTimeSelect = false,
  className = ''
}) => {
  const [isFocused, setIsFocused] = useState(false);

  const handleChange = (e) => {
    onChange(e.target.value);
  };

  const getInputType = () => {
    return showTimeSelect ? 'datetime-local' : 'date';
  };

  const formatMinDate = (date) => {
    if (!date) return '';
    
    const localDate = new Date(date.getTime() - (date.getTimezoneOffset() * 60000));
    return localDate.toISOString().slice(0, showTimeSelect ? 16 : 10);
  };

  return (
    <div className={`form-group ${className}`}>
      <label className="form-label">
        {label}
        {required && <span className="required">*</span>}
      </label>
      
      <div className={`datepicker-wrapper ${isFocused ? 'focused' : ''} ${error ? 'error' : ''}`}>
        <input
          type={getInputType()}
          value={value}
          onChange={handleChange}
          onFocus={() => setIsFocused(true)}
          onBlur={() => setIsFocused(false)}
          min={minDate ? formatMinDate(minDate) : undefined}
          className="datepicker-input"
          required={required}
        />
      </div>
      
      {error && <div className="error-message">{error}</div>}
    </div>
  );
};

export default DatePicker;