import { useState, useEffect } from 'react'
import './EngagementForm.css'

const EngegementForm = ({
items,newParticipants,handleAddNewParticipant,
    handleCancelNewParticipant,clearAllParticipants,editingId,handleDeleteExistingParticipant,
handleNewParticipantChange,handleSaveNewParticipant,handleStartEdit,handleCancelEdit,
handleExistingParticipantChange,handleSaveEdit}) => {

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



    // Save edited pa

    // Format date for display
    const formatDate = (dateString) => {
        return new Date(dateString).toLocaleDateString();
    };

    // Render existing participant (read-only view)
    const renderExistingParticipant = (participant, index) => {
        const displayName = participant.userName || `User #${participant.userId}`;
        
        return (
            <div className="participant-display existing">
                <div className="participant-main">
                    <strong className="participant-name">{displayName}</strong>
                    <span className="participant-id">ID: {participant.id}</span>
                </div>
                <div className="participant-details">
                    <span className="type-badge">{participant.userType}</span>
                    <span className="role-badge">{participant.participantRole}</span>
                    <span className="status-badge">{participant.status}</span>
                </div>
                <div className="participant-meta">
                    <span className="joined-date">Joined: {formatDate(participant.joinedAt)}</span>
                </div>
            </div>
        );
    };

    // Render editable form for existing participant
    const renderEditParticipantForm = (participant, index) => {
        return (
            <div className="participant-form edit-participant">
                <div className="form-header">
                    <h4>Edit Participant {index + 1}</h4>
                </div>
                
                <div className="form-grid">
                    <div className="form-group">
                        <label className="form-label required">User ID</label>
                        <input
                            type="text"
                            value={participant.userId}
                            onChange={(e) => handleExistingParticipantChange(participant.id, 'userId', e.target.value)}
                            placeholder="Enter user ID"
                            className="form-input"
                        />
                    </div>

                    <div className="form-group">
                        <label className="form-label required">User Type</label>
                        <select
                            value={participant.userType}
                            onChange={(e) => handleExistingParticipantChange(participant.id, 'userType', e.target.value)}
                            className="form-select"
                        >
                            <option value="">Select user type</option>
                            {userTypeOptions.map(option => (
                                <option key={option.value} value={option.value}>
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label className="form-label required">Participant Role</label>
                        <select
                            value={participant.participantRole}
                            onChange={(e) => handleExistingParticipantChange(participant.id, 'participantRole', e.target.value)}
                            className="form-select"
                        >
                            <option value="">Select role</option>
                            {participantRoleOptions.map(option => (
                                <option key={option.value} value={option.value}>
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label className="form-label">Status</label>
                        <select
                            value={participant.status}
                            onChange={(e) => handleExistingParticipantChange(participant.id, 'status', e.target.value)}
                            className="form-select"
                        >
                            <option value="INVITED">Invited</option>
                            <option value="ACCEPTED">Accepted</option>
                            <option value="DECLINED">Declined</option>
                            <option value="ATTENDED">Attended</option>
                        </select>
                    </div>
                </div>

                <div className="form-actions">
                    <button
                        type="button"
                        className="btn-save"
                        onClick={() => handleSaveEdit(participant.id)}
                    >
                        Save Changes
                    </button>
                    <button
                        type="button"
                        className="btn-cancel"
                        onClick={() => handleCancelEdit(participant.id)}
                    >
                        Cancel
                    </button>
                </div>
            </div>
        );
    };

    // Render new participant form (editable)
    const renderNewParticipantForm = (participant, index) => {
        return (
            <div className="participant-form new-participant">
                <div className="form-header">
                    <h4>New Participant {index + 1}</h4>
                </div>
                
                <div className="form-grid">
                    <div className="form-group">
                        <label className="form-label required">User ID</label>
                        <input
                            type="text"
                            value={participant.userId}
                            onChange={(e) => handleNewParticipantChange(index, 'userId', e.target.value)}
                            placeholder="Enter user ID"
                            className="form-input"
                        />
                    </div>

                    <div className="form-group">
                        <label className="form-label required">User Type</label>
                        <select
                            value={participant.userType}
                            onChange={(e) => handleNewParticipantChange(index, 'userType', e.target.value)}
                            className="form-select"
                        >
                            <option value="">Select user type</option>
                            {userTypeOptions.map(option => (
                                <option key={option.value} value={option.value}>
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label className="form-label required">Participant Role</label>
                        <select
                            value={participant.participantRole}
                            onChange={(e) => handleNewParticipantChange(index, 'participantRole', e.target.value)}
                            className="form-select"
                        >
                            <option value="">Select role</option>
                            {participantRoleOptions.map(option => (
                                <option key={option.value} value={option.value}>
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="form-actions">
                    <button
                        type="button"
                        className="btn-save"
                        onClick={() => handleSaveNewParticipant(index)}
                    >
                        Save Participant
                    </button>
                    <button
                        type="button"
                        className="btn-cancel"
                        onClick={() => handleCancelNewParticipant(index)}
                    >
                        Cancel
                    </button>
                </div>
            </div>
        );
    };

    return (
        <div className="participant-form-container">
            <div className="form-header">
                <h3>Participants Management</h3>
                <div className="header-actions">
                    <button 
                        type="button" 
                        className="btn-add"
                        onClick={handleAddNewParticipant}
                    >
                        + Add New Participant
                    </button>
                    {(items.length > 0 || newParticipants.length > 0) && (
                        <button 
                            type="button" 
                            className="btn-clear"
                            onClick={clearAllParticipants}
                        >
                            Clear All
                        </button>
                    )}
                </div>
            </div>

            {/* New Participant Forms */}
            {newParticipants.length > 0 && (
                <div className="new-participants-section">
                    <h4>New Participants ({newParticipants.length})</h4>
                    {newParticipants.map((participant, index) => (
                        <div key={participant.id} className="participant-row new">
                            {renderNewParticipantForm(participant, index)}
                        </div>
                    ))}
                </div>
            )}

            {/* Existing Participants */}
            <div className="existing-participants-section">
                <h4>Existing Participants ({items.length})</h4>
                
                {items.length === 0 && newParticipants.length === 0 ? (
                    <div className="empty-state">
                        <p>No participants added yet. Click "Add New Participant" to get started.</p>
                    </div>
                ) : (
                    <div className="items-list">
                        {items.map((item, index) => (
                            <div key={item.id} className="participant-row existing">
                                <div className="item-content">
                                    <span className="item-index">{index + 1}.</span>
                                    <div className="item-text">
                                        {editingId === item.id 
                                            ? renderEditParticipantForm(item, index)
                                            : renderExistingParticipant(item, index)
                                        }
                                    </div>
                                </div>
                                <div className="participant-actions">
                                    {editingId === item.id ? (
                                        <div className="edit-actions">
                                            {/* Save and Cancel are inside the form */}
                                        </div>
                                    ) : (
                                        <>
                                            <button
                                                type="button"
                                                className="btn-edit"
                                                onClick={() => handleStartEdit(item.id)}
                                                title="Edit participant"
                                            >
                                                ✏️
                                            </button>
                                            <button
                                                type="button"
                                                className="btn-delete"
                                                onClick={() => handleDeleteExistingParticipant(index)}
                                                title="Delete participant"
                                            >
                                                ×
                                            </button>
                                        </>
                                    )}
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    )
}

export default EngegementForm