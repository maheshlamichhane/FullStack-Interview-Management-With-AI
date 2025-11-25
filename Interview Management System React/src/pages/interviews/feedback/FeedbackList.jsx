import './FeedbackList.css'
const FeedbackList = ({ feedbackResponses }) => {
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
    <div className="feedback-section">
      <h4>Feedback Responses ({feedbackResponses.length})</h4>
      {feedbackResponses.length === 0 ? (
        <p className="no-feedback">No feedback submitted yet.</p>
      ) : (
        feedbackResponses.map((feedback) => (
          <div key={feedback.id} className="feedback-card">
            <div className="feedback-meta">
              <span>Response #{feedback.id}</span>
              <span>Submitted: {formatDate(feedback.submittedAt)}</span>
            </div>
            {feedback.rating && (
              <div className="feedback-rating">
                Rating: {feedback.rating}/5
              </div>
            )}
            {feedback.feedbackText && (
              <p className="feedback-text">"{feedback.feedbackText}"</p>
            )}
          </div>
        ))
      )}
    </div>
  )
}
export default FeedbackList