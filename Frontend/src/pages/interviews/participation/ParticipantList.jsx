import './ParticipantList.css'
const ParticipantsList = ({ participants }) => {
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  return (
    <div className="participants-section">
      <h4>Participants ({participants.length})</h4>
      <div className="participants-grid">
        {participants.map((participant) => (
          <div key={participant.id} className="participant-card">
            <div className="participant-details">
              <span className="user-id">User #{participant.userId}</span>
              <span className="user-type">{participant.userType}</span>
              <span className="participant-role">{participant.participantRole}</span>
              <span className="participant-status">{participant.status}</span>
            </div>
            <div className="participant-meta">
              Joined: {formatDate(participant.joinedAt)}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
export default ParticipantsList