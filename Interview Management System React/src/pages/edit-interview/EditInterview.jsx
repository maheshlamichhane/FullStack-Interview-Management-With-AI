import './EditInterview.css'
import Users from '../../components/interview form/users/Users'
import Calendar from '../../components/calendar/Calendar'
import DatePicker from '../../components/datepicker/DatePicker'
import Clock from '../../components/clock/Clock'
import Input from '../../components/input/Input'
import Select from '../../components/select/Select'
import { useState} from 'react'
import { useLocation,useNavigate } from 'react-router-dom'
import EngegementForm from './EngagmentForm/EngagementForm'
import Button from '../../components/button/Button'

const EditInterview = () => {

    const navigate = useNavigate();
    const location = useLocation();
    const {interview} = location.state || {};

  const [formData, setFormData] = useState(interview || {});
  const [items,setItems] = useState(formData.participants || []);
  const [newParticipants, setNewParticipants] = useState([]);
 const [editingId, setEditingId] = useState(null); 
  const [isSubmitting, setIsSubmitting] = useState(false);


     const handleAddNewParticipant = () => {
        const newParticipant = {
            id: null,
            userId: '',
            userName: '',
            userEmail: '',
            userType: '',
            participantRole: '',
            status: 'INVITED',
            isNew: true
        };
        setNewParticipants(prev => [...prev, newParticipant]);
    };

      const handleCancelNewParticipant = (index) => {
        setNewParticipants(prev => prev.filter((_, i) => i !== index));
    };

       const clearAll = () => {
        setNewParticipants([]);
        setEditingId(null);
    };

       const handleDelete = (index) => {
        const updatedItems = items.filter((_, i) => i !== index);
        setItems(updatedItems);

    };

       // Update new participant data
    const handleNewParticipantChange = (index, field, value) => {
        setNewParticipants(prev => 
            prev.map((participant, i) => 
                i === index ? { ...participant, [field]: value } : participant
            )
        );
    };

        // Save new participant
    const handleSaveNewParticipant = (index) => {
        const participant = newParticipants[index];
        
        // Basic validation
        if (!participant.userId || !participant.userType || !participant.participantRole) {
            alert('Please fill in all required fields: User ID, User Type, and Participant Role');
            return;
        }

        const newParticipant = {
            ...participant,
            id: null,
            joinedAt: new Date().toISOString(),
            createdAt: new Date().toISOString(),
            isNew: false
        };

        // Add to main items and remove from new participants
        const updatedItems = [...items, newParticipant];
        setItems(updatedItems);
        setNewParticipants(prev => prev.filter((_, i) => i !== index));
      
    };

  const handleStartEdit = (id) => {
        setEditingId(id);
  };

      const handleCancelEdit = (id) => {
        setEditingId(null);
    };

       // Update existing participant data (when editing)
    const handleExistingParticipantChange = (id, field, value) => {
        setItems(prev => 
            prev.map(participant => 
                participant.id === id ? { ...participant, [field]: value } : participant
            )
        );
    };

      const handleSaveEdit = (id) => {
        const participant = items.find(p => p.id === id);
        
        // Validation
        if (!participant.userId || !participant.userType || !participant.participantRole) {
            alert('Please fill in all required fields: User ID, User Type, and Participant Role');
            return;
        }
        setEditingId(null);
    };

const handleInputChange = (field, value) => {
    setFormData(prevData => ({
      ...prevData,
      [field]: value
    }));
};  

    const handleUpdateInterview = async (event) => {
    event.preventDefault();

    setIsSubmitting(true);
    
    try {
      // Simulate API call

      formData.participants = items;
      console.log('Submitting interview data:', formData);

      // await createInterviewAPI(formData);
      const response  = await fetch('http://localhost:8080/api/v1/interviews' ,{
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData),
    });
    const result = await response.json();
      console.log( result.message);
      alert(result.message);
      navigate('/interviews'); 
      
    } catch (error) {
      console.error('Error creating interview:', error);
      alert('Error creating interview. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
    }

    
  
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

  return(
 <div className="interview-form-container">
      <div className="form-header">
        <h1>Update Interview</h1>
        <p>Fill in the details to update as schedule interview session</p>
      </div>

      <form onSubmit={null} className="interview-form">
        <div className="form-section">
          <h2 className="section-title">
            <Users size={20} />
            Basic Information
          </h2>
          
          <div className="form-grid">
            <Input
              value = {formData.title}
              label="Interview Title *"
              onChange={(value) => handleInputChange('title', value)}
              placeholder="Enter interview title"
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
              value={formData.scheduledTime}
              label="Scheduled Time *"
              onChange={(value) => handleInputChange('scheduledTime', value)}
              minDate={new Date()}
              showTimeSelect
              required
            />

            <Select
            value={formData.durationMinutes}
              label="Duration"
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
            value={formData.meetingUrl}
            label="Meeting URL"
            onChange={(value) => handleInputChange('meetingUrl', value)}
            placeholder="https://meet.google.com/abc-def-ghi"
            type="url"
          />
        </div>

        <EngegementForm 
        handleAddNewParticipant={handleAddNewParticipant}
        handleCancelNewParticipant= {handleCancelNewParticipant}
        clearAllParticipants = {clearAll}
        handleDeleteExistingParticipant={handleDelete}
        handleNewParticipantChange={handleNewParticipantChange}
        handleSaveNewParticipant={handleSaveNewParticipant}
        handleStartEdit = {handleStartEdit}
        handleCancelEdit = {handleCancelEdit}
        handleExistingParticipantChange = {handleExistingParticipantChange}
        handleSaveEdit = {handleSaveEdit}
   
        onItemsChange = {null}

        items={items}
        newParticipants={newParticipants}
        editingId={editingId}
      
        />

        <div className="form-grid">
          <Button onClick={(event) => handleUpdateInterview(event)}>Update Interview</Button>
         </div>

        </form>
        </div>
  );
}
export default EditInterview