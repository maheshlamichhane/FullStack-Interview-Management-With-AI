import React, { useState, useEffect } from 'react'
import Interview from './interview/Interview'
import './Interviews.css'

const Interviews = () => {
  const [interviews, setInterviews] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchInterviews = async () => {
      try {
        setLoading(true)
        setError(null)
        
        const response = await fetch('http://localhost:8080/api/v1/interviews')
        
        if (!response.ok) {
          throw new Error(`Failed to fetch interviews: ${response.status}`)
        }
        
        const data = await response.json()
        setInterviews(data)
        
      } catch (err) {
        setError(err.message)
        console.error('Failed to fetch interviews:', err)
      } finally {
        setLoading(false)
      }
    }

    fetchInterviews()
  }, []) // Empty dependency array since we're not using get hook

  if (loading) {
    return (
      <div className="interviews">
        <div className="loading">Loading interviews...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="interviews">
        <div className="error">Error loading interviews: {error}</div>
      </div>
    )
  }

  return (
    <div className="interviews">
      <div className="interviews-header">
        <h1>Interviews</h1>
        <p>Manage and view all scheduled interviews</p>
      </div>

      <div className="interviews-list">
        {interviews.length === 0 ? (
          <div className="no-interviews">
            No interviews found. Schedule your first interview!
          </div>
        ) : (
          interviews.map((interview) => (
            <Interview 
              key={interview.id} 
              interview={interview}
            />
          ))
        )}
      </div>
    </div>
  )
}

export default Interviews