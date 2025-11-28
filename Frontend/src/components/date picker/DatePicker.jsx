// src/components/UI/DatePicker.jsx
import { useState, useRef, useEffect } from 'react';
import { Calendar, Clock, ChevronLeft, ChevronRight } from 'lucide-react';

const DatePicker = ({
  label,
  value,
  onChange,
  error,
  required = false,
  minDate,
  maxDate,
  showTimeSelect = false,
  timeFormat = 'HH:mm',
  dateFormat = 'yyyy-MM-dd',
  placeholder = "Select date and time",
  ...props
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedDate, setSelectedDate] = useState(value ? new Date(value) : null);
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [selectedTime, setSelectedTime] = useState('09:00');
  const dropdownRef = useRef(null);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Initialize selected time from value
  useEffect(() => {
    if (value) {
      const date = new Date(value);
      setSelectedDate(date);
      setSelectedTime(
        `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
      );
    }
  }, [value]);

  const getDaysInMonth = (date) => {
    return new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
  };

  const getFirstDayOfMonth = (date) => {
    return new Date(date.getFullYear(), date.getMonth(), 1).getDay();
  };

  const isDateDisabled = (date) => {
    if (minDate && date < new Date(minDate.setHours(0, 0, 0, 0))) return true;
    if (maxDate && date > new Date(maxDate.setHours(23, 59, 59, 999))) return true;
    return false;
  };

  const isDateSelected = (date) => {
    if (!selectedDate) return false;
    return (
      date.getDate() === selectedDate.getDate() &&
      date.getMonth() === selectedDate.getMonth() &&
      date.getFullYear() === selectedDate.getFullYear()
    );
  };

  const handleDateSelect = (day) => {
    const newDate = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), day);
    setSelectedDate(newDate);
    
    if (!showTimeSelect) {
      const formattedDate = formatDate(newDate);
      onChange(formattedDate);
      setIsOpen(false);
    }
  };

  const handleTimeChange = (time) => {
    setSelectedTime(time);
  };

  const handleConfirm = () => {
    if (selectedDate) {
      let finalDate = new Date(selectedDate);
      
      if (showTimeSelect) {
        const [hours, minutes] = selectedTime.split(':').map(Number);
        finalDate.setHours(hours, minutes, 0, 0);
      } else {
        finalDate.setHours(12, 0, 0, 0);
      }

      const formattedDate = formatDate(finalDate);
      onChange(formattedDate);
      setIsOpen(false);
    }
  };

  const formatDate = (date) => {
    // Format as yyyy-MM-dd HH:mm:ss for Java backend
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  };

  const formatDisplayDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    
    if (showTimeSelect) {
      return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
    
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  const navigateMonth = (direction) => {
    setCurrentMonth(prev => {
      const newMonth = new Date(prev);
      newMonth.setMonth(prev.getMonth() + direction);
      return newMonth;
    });
  };

  const generateCalendarDays = () => {
    const daysInMonth = getDaysInMonth(currentMonth);
    const firstDay = getFirstDayOfMonth(currentMonth);
    const days = [];

    // Previous month days
    const prevMonth = new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1, 1);
    const prevMonthDays = getDaysInMonth(prevMonth);
    
    for (let i = firstDay - 1; i >= 0; i--) {
      const day = prevMonthDays - i;
      const date = new Date(prevMonth.getFullYear(), prevMonth.getMonth(), day);
      days.push({ day, isCurrentMonth: false, date, disabled: isDateDisabled(date) });
    }

    // Current month days
    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), day);
      days.push({ day, isCurrentMonth: true, date, disabled: isDateDisabled(date) });
    }

    // Next month days (to fill the grid)
    const totalCells = 42; // 6 weeks * 7 days
    let nextMonthDay = 1;
    while (days.length < totalCells) {
      const date = new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, nextMonthDay);
      days.push({ day: nextMonthDay, isCurrentMonth: false, date, disabled: isDateDisabled(date) });
      nextMonthDay++;
    }

    return days;
  };

  const generateTimeSlots = () => {
    const slots = [];
    for (let hour = 9; hour <= 17; hour++) { // 9 AM to 5 PM
      for (let minute = 0; minute < 60; minute += 30) { // Every 30 minutes
        const timeString = `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
        slots.push(timeString);
      }
    }
    return slots;
  };

  const calendarDays = generateCalendarDays();
  const timeSlots = generateTimeSlots();

  return (
    <div className="form-group">
      <label className="form-label">
        {label}
        {required && <span className="required">*</span>}
      </label>
      
      <div className="datepicker-wrapper" ref={dropdownRef}>
        <button
          type="button"
          className={`datepicker-input ${error ? 'error' : ''} ${isOpen ? 'active' : ''}`}
          onClick={() => setIsOpen(!isOpen)}
        >
          <Calendar size={18} />
          <span className="datepicker-display">
            {value ? formatDisplayDate(value) : placeholder}
          </span>
        </button>

        {isOpen && (
          <div className="datepicker-dropdown">
            {/* Calendar Header */}
            <div className="calendar-header">
              <button
                type="button"
                className="nav-button"
                onClick={() => navigateMonth(-1)}
              >
                <ChevronLeft size={16} />
              </button>
              
              <div className="current-month">
                {currentMonth.toLocaleDateString('en-US', { 
                  month: 'long', 
                  year: 'numeric' 
                })}
              </div>
              
              <button
                type="button"
                className="nav-button"
                onClick={() => navigateMonth(1)}
              >
                <ChevronRight size={16} />
              </button>
            </div>

            {/* Calendar Grid */}
            <div className="calendar-grid">
              {/* Week days header */}
              {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map(day => (
                <div key={day} className="week-day-header">
                  {day}
                </div>
              ))}
              
              {/* Calendar days */}
              {calendarDays.map(({ day, isCurrentMonth, date, disabled }, index) => (
                <button
                  key={index}
                  type="button"
                  className={`calendar-day 
                    ${!isCurrentMonth ? 'other-month' : ''} 
                    ${isDateSelected(date) ? 'selected' : ''}
                    ${disabled ? 'disabled' : ''}
                  `}
                  onClick={() => !disabled && handleDateSelect(day)}
                  disabled={disabled}
                >
                  {day}
                </button>
              ))}
            </div>

            {/* Time Selector */}
            {showTimeSelect && (
              <div className="time-selector">
                <div className="time-selector-header">
                  <Clock size={16} />
                  <span>Select Time</span>
                </div>
                <div className="time-slots">
                  {timeSlots.map(time => (
                    <button
                      key={time}
                      type="button"
                      className={`time-slot ${selectedTime === time ? 'selected' : ''}`}
                      onClick={() => handleTimeChange(time)}
                    >
                      {time}
                    </button>
                  ))}
                </div>
              </div>
            )}

            {/* Action Buttons */}
            <div className="datepicker-actions">
              <button
                type="button"
                className="btn btn-outline"
                onClick={() => setIsOpen(false)}
              >
                Cancel
              </button>
              <button
                type="button"
                className="btn btn-primary"
                onClick={handleConfirm}
                disabled={!selectedDate}
              >
                Confirm
              </button>
            </div>
          </div>
        )}
      </div>

      {error && <div className="error-message">{error}</div>}
    </div>
  );
};

export default DatePicker;