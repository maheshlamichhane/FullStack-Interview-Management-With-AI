import ParticipantsList from '../participation/ParticipantList'
import FeedbackList from '../feedback/FeedbackList'
import Button from '../../../components/button/Button'
import './Interview.css'
import { useNavigate } from 'react-router-dom'

const Interview = ({ interview,participants,feedbacks}) => {

  const navigate = useNavigate();

  const handleUpdateRedirection = (interviewId) => {
     navigate(`/interviews/${interview.id}/edit`, {
      state: { 
        interview: interview
      }
    });
  };
  
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  // Get status badge style
  const getStatusStyle = (status) => {
    switch (status) {
      case 'DRAFT':
        return { background: '#6b7280', color: 'white' }
      case 'SCHEDULED':
        return { background: '#3b82f6', color: 'white' }
      case 'COMPLETED':
        return { background: '#10b981', color: 'white' }
      case 'CANCELLED':
        return { background: '#ef4444', color: 'white' }
      default:
        return { background: '#6b7280', color: 'white' }
    }
  }

 const handleDelete = async (event,interviewId) => {
    if (event) {
      event.preventDefault();
    }

    if (!window.confirm('Are you sure you want to delete this interview? This action cannot be undone.')) {
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/api/v1/interviews/${interviewId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        }
      })

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}))
        throw new Error(errorData.message || `Failed to delete interview: ${response.status}`)
      }

      // // Remove the interview from the list
      // setInterviews(prevInterviews => 
      //   prevInterviews.filter(interview => interview.id !== interviewId)
      // )
      
      console.log(`Interview ${interviewId} deleted successfully`)
      alert(`Interview ${interviewId} deleted successfully`)
      
    } catch (err) {
      console.error('Error deleting interview:', err)
      alert(`Failed to delete interview: ${err.message}`)
    } finally {
      setDeletingId(null)
    }
  }




  return (
    <div className="interview-card">
      <div className="card-header">
        <div className="title-section">
          <h3 className="interview-title">{interview.title}</h3>
          <span 
            className="status-badge"
            style={getStatusStyle(interview.status)}
          >
            {interview.status}
          </span>
        </div>
        <div className="interview-meta">
          <span className="interview-id">ID: {interview.id}</span>
          <span className="created-by">By: {interview.createdBy}</span>
        </div>
      </div>

      {/* Interview Body */}
      <div className="card-body">
        <p className="interview-description">{interview.description}</p>
        
        <div className="interview-details">
          <div className="detail-row">
            <strong>When:</strong> {formatDate(interview.scheduledTime)}
          </div>
          <div className="detail-row">
            <strong>Duration:</strong> {interview.durationMinutes} minutes
          </div>
          <div className="detail-row">
            <strong>Meeting:</strong> 
            <a 
              href={interview.meetingUrl} 
              target="_blank" 
              rel="noopener noreferrer"
              className="meeting-link"
            >
              Join Meeting
            </a>
          </div>
        </div>

        {/* Participants */}
        <ParticipantsList participants={interview.participants} />

        {/* Feedback */}
        <FeedbackList feedbackResponses={interview.feedBackResponses} />
      </div>

      {/* Card Footer */}
      <div className="card-footer">
        <div className="action-buttons">
          <Button 
            variant="primary"
            icon="✏️"
            className="btn-update"
            onClick={() => handleUpdateRedirection(interview.id)}
          >
            Update
          </Button>
       <Button 
        variant="secondary"
        icon="🗑️"
        onClick={(e) => handleDelete(e, interview.id)}
        className="btn-delete"
>
  Delete
</Button>
        </div>
        <div className="timestamps">
          <small>Created: {formatDate(interview.createdAt)}</small>
          <small>Updated: {formatDate(interview.updatedAt)}</small>
        </div>
      </div>
    </div>
  )
}
export default Interview