import {useEffect, useState} from "react"
import Button from '../button/Button'
import Input from '../input/Input'
import Select from '../select/Select'
import ParticipantSection from "./participant/ParticipantSection"
import Users from "./users/Users"
import Calendar from "../calendar/Calendar"
import DatePicker from "../datepicker/DatePicker"
import Clock from "../clock/Clock"
import  './InterviewForm.css'


const InterviewForm = () => {


  const [formData, setFormData] = useState({
    title: '',
    description: '',
    scheduledTime: '',
    durationMinutes: 60,
    meetingUrl: '',
    participants: []
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);



  // User type options
  const userTypeOptions = [
    { value: 'RECRUITER', label: 'Recruiter' },
    { value: 'INTERVIEWER', label: 'Interviewer' },
    { value: 'CANDIDATE', label: 'Candidate' }
  ];

  // Participant role options
  const participantRoleOptions = [
    { value: 'OWNER', label: 'Owner' },
    { value: 'PARTICIPANT', label: 'Participant' },
    { value: 'OBSERVER', label: 'Observer' }
  ];

  // Duration options
  const durationOptions = [
    { value: 30, label: '30 minutes' },
    { value: 45, label: '45 minutes' },
    { value: 60, label: '1 hour' },
    { value: 90, label: '1.5 hours' },
    { value: 120, label: '2 hours' }
  ];

  const validateForm = () => {
    const newErrors = {};

    // Required field validation
    if (!formData.title.trim()) {
      newErrors.title = 'Interview title is required';
    }

    if (!formData.scheduledTime) {
      newErrors.scheduledTime = 'Scheduled time is required';
    } else if (new Date(formData.scheduledTime) <= new Date()) {
      newErrors.scheduledTime = 'Scheduled time must be in the future';
    }

    if (formData.participants.length === 0) {
      newErrors.participants = 'At least one participant is required';
    }

    // Validate participants
    formData.participants.forEach((participant, index) => {
      if (!participant.userId) {
        newErrors[`participants[${index}].userId`] = 'User ID is required';
      }
      if (!participant.userType) {
        newErrors[`participants[${index}].userType`] = 'User type is required';
      }
      if (!participant.participantRole) {
        newErrors[`participants[${index}].participantRole`] = 'Participant role is required';
      }
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setIsSubmitting(true);

        const interviewData = {
        title: formData.title.trim(),
        description: formData.description.trim(),
        scheduledTime: formData.scheduledTime,
        durationMinutes: formData.durationMinutes,
        meetingUrl: formData.meetingUrl.trim(),
        participants: formData.participants.map(participant => ({
          userId: parseInt(participant.userId),
          userType: participant.userType,
          participantRole: participant.participantRole
        }))
      };

     try {
            // Send via REST API
            const apiResponse = await fetch('http://localhost:8080/api/v1/interviews', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(interviewData),
            });
            
            const result = await apiResponse.json();
        } catch (error) {
            console.error('❌ Error:', error);
            if (error.message === 'Request timeout') {
                alert('Server is taking too long to respond. Please check if the interview was created.');
            } else {
                alert('Error creating interview: ' + error.message);
            }
        }
        finally{
          setIsSubmitting(false);
        }
  };

  const handleInputChange = (field, value) => {
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

  const handleAddParticipant = () => {
    setFormData(prev => ({
      ...prev,
      participants: [
        ...prev.participants,
        { userId: '', userType: '', participantRole: '' }
      ]
    }));
  };

  const handleParticipantChange = (index, field, value) => {
    setFormData(prev => ({
      ...prev,
      participants: prev.participants.map((participant, i) =>
        i === index ? { ...participant, [field]: value } : participant
      )
    }));

    // Clear participant error
    const errorKey = `participants[${index}].${field}`;
    if (errors[errorKey]) {
      setErrors(prev => ({
        ...prev,
        [errorKey]: ''
      }));
    }
  };

  const handleRemoveParticipant = (index) => {
    setFormData(prev => ({
      ...prev,
      participants: prev.participants.filter((_, i) => i !== index)
    }));
  };

  return (
    <div className=".interview-form-container">
      <div className="form-header">
        <h1>Schedule New Interview</h1>
        <p>Fill in the details to </p>
      </div>

      <form onSubmit={handleSubmit} className="interview-form">
        <div className="form-section">
          <h2 className="section-title">
            <Users size={20} />
            Basic Information
          </h2>
          
          <div className="form-grid">
            <Input
              label="Interview Title *"
              value={formData.title}
              onChange={(value) => handleInputChange('title', value)}
              placeholder="Enter interview title"
              error={errors.title}
              required
            />

            <div className="form-group">
              <label className="form-label">Description</label>
              <textarea
                value={formData.description}
                onChange={(e) => handleInputChange('description', e.target.value)}
                placeholder="Enter interview description"
                className="form-textarea"
                rows="3"
              />
            </div>
          </div>
        </div>

        {/* Schedule Section */}
        <div className="form-section">
          <h2 className="section-title">
            <Calendar size={20} />
            Schedule
          </h2>
          
          <div className="form-grid">
            <DatePicker
              label="Scheduled Time *"
              value={formData.scheduledTime}
              onChange={(value) => handleInputChange('scheduledTime', value)}
              error={errors.scheduledTime}
              minDate={new Date()}
              showTimeSelect
              required
            />

            <Select
              label="Duration"
              value={formData.durationMinutes}
              onChange={(value) => handleInputChange('durationMinutes', parseInt(value))}
              options={durationOptions}
              icon={<Clock size={16} />}
            />
          </div>
        </div>

        {/* Meeting Details Section */}
        <div className="form-section">
          <h2 className="section-title">
            <Users size={20} />
            Meeting Details
          </h2>
          
          <Input
            label="Meeting URL"
            value={formData.meetingUrl}
            onChange={(value) => handleInputChange('meetingUrl', value)}
            placeholder="https://meet.google.com/abc-def-ghi"
            type="url"
          />
        </div>

        {/* Participants Section */}
        <ParticipantSection
          participants={formData.participants}
          onAddParticipant={handleAddParticipant}
          onRemoveParticipant={handleRemoveParticipant}
          onParticipantChange={handleParticipantChange}
          userTypeOptions={userTypeOptions}
          participantRoleOptions={participantRoleOptions}
          errors={errors}
        />

        {/* Form Actions */}
        <div className="form-actions">
          <Button
            type="button"
            variant="outline"
            onClick={() => window.history.back()}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            loading={isSubmitting}
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Creating Interview...' : 'Create Interview'}
          </Button>
        </div>
      </form>
    </div>
  );
};

export default InterviewForm;