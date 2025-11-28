const Select = ({
  label,
  value,
  onChange,
  options,
  error,
  required = false,
  icon,
  ...props
}) => {
  return (
    <div className="form-group">
      <label className="form-label">
        {label}
        {required && <span className="required">*</span>}
      </label>
      <div className="select-wrapper">
        {icon && <span className="select-icon">{icon}</span>}
        <select
          value={value}
          onChange={(e) => onChange(e.target.value)}
          className={`form-select ${error ? 'error' : ''}`}
          {...props}
        >
          <option value="">Select {label.toLowerCase()}</option>
          {options.map(option => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>
      {error && <div className="error-message">{error}</div>}
    </div>
  );
};

export default Select;