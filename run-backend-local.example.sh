#!/bin/bash
# ============================================================
# TEMPLATE — copy this file to run-backend-local.sh,
# fill in YOUR real credentials, then run:
#   chmod +x run-backend-local.sh && ./run-backend-local.sh
#
# run-backend-local.sh is gitignored, so your real secrets
# will never be committed or pushed.
# ============================================================
set -e
cd "$(dirname "$0")/backend spring boot"

# ---- Database ----
export DB_PASSWORD='your_mysql_password'

# ---- Google OAuth (create at https://console.cloud.google.com) ----
export GOOGLE_CLIENT_ID='your_google_client_id'
export GOOGLE_CLIENT_SECRET='your_google_client_secret'

# ---- Razorpay (create at https://dashboard.razorpay.com) ----
export RAZORPAY_API_KEY='your_razorpay_key'
export RAZORPAY_API_SECRET='your_razorpay_secret'

echo "✅ All credentials exported:"
echo "   DB_PASSWORD, GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, RAZORPAY_API_KEY, RAZORPAY_API_SECRET"
echo "▶ Starting Spring Boot backend on http://localhost:5454 ..."
echo
./mvnw spring-boot:run
