#!/bin/bash
# infinite-monitor-fixed.sh

BASE_URL="http://api.interview-app.com"
HOST="api.interview-app.com"
COUNTER=1

# Clear screen and show header
clear
echo "┌─────────────────────────────────────────────────────┐"
echo "│        INFINITE API TEST - CIRCUIT BREAKER          │"
echo "│        Press Ctrl+C to stop                         │"
echo "└─────────────────────────────────────────────────────┘"
echo ""

while true; do
  # Make request and capture output
  response=$(curl -s -i -X GET "$BASE_URL/api/ai/demo" \
    -H "Host: $HOST" \
    -w "\nTIME: %{time_total}s")
  
  # Extract HTTP status - FIXED for all grep versions
  http_code=$(echo "$response" | head -1 | grep -oE '[0-9]{3}')
  
  # Extract body
  body=$(echo "$response" | awk '/^\r?$/{body=1; next} body' | head -1)
  
  # Extract time
  time_taken=$(echo "$response" | grep "TIME:" | awk '{print $2}')
  
  # Clean body for display (limit length)
  display_body="${body:0:30}"
  
  # Color coding
  if [[ $http_code -eq 500 ]]; then
    color="\033[31m"  # Red for 500
    status="🔴 FAIL"
  else
    color="\033[32m"  # Green for others
    status="🟢 OK"
  fi
  
  # Display in a table format - FIXED printf
  printf "│ %6d │ %s │ ${color}%3s${NC} │ %-30s │ %6s │\n" \
    $COUNTER "$(date '+%H:%M:%S')" "$http_code" "$display_body" "$time_taken"
  
  ((COUNTER++))
  sleep 1
done
