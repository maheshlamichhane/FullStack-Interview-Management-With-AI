import Users from '../users/Users';
const ParticipantSection = ({
  participants,
  onAddParticipant,
  onRemoveParticipant,
  onParticipantChange,
  userTypeOptions,
  participantRoleOptions,
  errors
}) => {
  return (
    <div className="form-section">
      <h2 className="section-title">
        <Users size={20} />
        Participants
      </h2>
      
      {participants.map((participant, index) => (
        <div key={index} className="participant-row">
          <div className="participant-header">
            <h3>Participant {index + 1}</h3>
            {participants.length > 1 && (
              <button
                type="button"
                className="remove-participant"
                onClick={() => onRemoveParticipant(index)}
              >
                Remove
              </button>
            )}
          </div>
          
          <div className="form-grid">
            <div className="form-group">
              <label className="form-label">User ID *</label>
              <input
                type="text"
                value={participant.userId}
                onChange={(e) => onParticipantChange(index, 'userId', e.target.value)}
                placeholder="Enter user ID"
                className={`form-input ${errors[`participants[${index}].userId`] ? 'error' : ''}`}
              />
              {errors[`participants[${index}].userId`] && (
                <div className="error-message">{errors[`participants[${index}].userId`]}</div>
              )}
            </div>

            <div className="form-group">
              <label className="form-label">User Type *</label>
              <select
                value={participant.userType}
                onChange={(e) => onParticipantChange(index, 'userType', e.target.value)}
                className={`form-select ${errors[`participants[${index}].userType`] ? 'error' : ''}`}
              >
                <option value="">Select user type</option>
                {userTypeOptions.map(option => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors[`participants[${index}].userType`] && (
                <div className="error-message">{errors[`participants[${index}].userType`]}</div>
              )}
            </div>

            <div className="form-group">
              <label className="form-label">Participant Role *</label>
              <select
                value={participant.participantRole}
                onChange={(e) => onParticipantChange(index, 'participantRole', e.target.value)}
                className={`form-select ${errors[`participants[${index}].participantRole`] ? 'error' : ''}`}
              >
                <option value="">Select role</option>
                {participantRoleOptions.map(option => (
                  <option key={option.value} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
              {errors[`participants[${index}].participantRole`] && (
                <div className="error-message">{errors[`participants[${index}].participantRole`]}</div>
              )}
            </div>
          </div>
        </div>
      ))}
      
      {errors.participants && (
        <div className="error-message">{errors.participants}</div>
      )}
      
      <button
        type="button"
        className="add-participant-btn"
        onClick={onAddParticipant}
      >
        + Add Participant
      </button>
    </div>
  );
};

export default ParticipantSection;