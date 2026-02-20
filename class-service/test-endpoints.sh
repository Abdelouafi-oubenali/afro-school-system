#!/bin/bash

# ============================================
# ENDPOINTS ABSENCE - TEST AVEC BODY
# ============================================

BASE_URL="http://localhost:8080/api/absences"

echo "========== 1. CREATE SINGLE ABSENCE (POST) =========="
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "eleveId": "550e8400-e29b-41d4-a716-446655440000",
    "enseignantId": "550e8400-e29b-41d4-a716-446655440001",
    "seanceId": "550e8400-e29b-41d4-a716-446655440010",
    "classeId": "550e8400-e29b-41d4-a716-446655440020",
    "date": "2026-02-19",
    "heureDebut": "14:00:00",
    "heureFin": "16:00:00",
    "type": "ABSENCE",
    "motif": "Absence non justifiée"
  }' | jq .

echo -e "\n========== 2. CREATE BULK ABSENCES (POST /bulk) =========="
curl -X POST $BASE_URL/bulk \
  -H "Content-Type: application/json" \
  -d '{
    "eleveIds": [
      "550e8400-e29b-41d4-a716-446655440000",
      "550e8400-e29b-41d4-a716-446655440002",
      "550e8400-e29b-41d4-a716-446655440003"
    ],
    "enseignantId": "550e8400-e29b-41d4-a716-446655440001",
    "seanceId": "550e8400-e29b-41d4-a716-446655440010",
    "classeId": "550e8400-e29b-41d4-a716-446655440020",
    "date": "2026-02-19",
    "heureDebut": "14:00:00",
    "heureFin": "16:00:00",
    "type": "ABSENCE",
    "motif": "Absence collective"
  }' | jq .

echo -e "\n========== 3. GET ALL ABSENCES (GET) =========="
curl -X GET $BASE_URL | jq .

echo -e "\n========== 4. GET ABSENCE BY ID (GET) =========="
curl -X GET $BASE_URL/550e8400-e29b-41d4-a716-446655440000 | jq .

echo -e "\n========== 5. GET ABSENCES BY ELEVE (GET) =========="
curl -X GET $BASE_URL/eleve/550e8400-e29b-41d4-a716-446655440000 | jq .

echo -e "\n========== 6. GET ABSENCES BY ENSEIGNANT (GET) =========="
curl -X GET $BASE_URL/enseignant/550e8400-e29b-41d4-a716-446655440001 | jq .

echo -e "\n========== 7. GET ABSENCES BY DATE (GET) =========="
curl -X GET $BASE_URL/date/2026-02-19 | jq .

echo -e "\n========== 8. GET ABSENCES BY ELEVE AND DATE (GET) =========="
curl -X GET $BASE_URL/eleve/550e8400-e29b-41d4-a716-446655440000/date/2026-02-19 | jq .

echo -e "\n========== 9. UPDATE ABSENCE (PUT) =========="
curl -X PUT $BASE_URL/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{
    "eleveId": "550e8400-e29b-41d4-a716-446655440000",
    "enseignantId": "550e8400-e29b-41d4-a716-446655440001",
    "seanceId": "550e8400-e29b-41d4-a716-446655440010",
    "classeId": "550e8400-e29b-41d4-a716-446655440020",
    "date": "2026-02-19",
    "heureDebut": "14:00:00",
    "heureFin": "16:00:00",
    "type": "RETARD",
    "motif": "Retard justifié"
  }' | jq .

echo -e "\n========== 10. DELETE ABSENCE (DELETE) =========="
curl -X DELETE $BASE_URL/550e8400-e29b-41d4-a716-446655440000
