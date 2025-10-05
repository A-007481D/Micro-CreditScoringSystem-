-- Microfinance Credit Scoring System - Table Verification Script
-- Run this after the app starts to verify all tables were created correctly

\c microfin

-- Check all tables exist
SELECT 'Tables Created:' as status;
SELECT tablename FROM pg_tables WHERE schemaname = 'public' ORDER BY tablename;

-- Verify employes table structure
SELECT 'Employes Table Structure:' as status;
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'employes' 
ORDER BY ordinal_position;

-- Verify professionnels table structure
SELECT 'Professionnels Table Structure:' as status;
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'professionnels' 
ORDER BY ordinal_position;

-- Verify credits table structure
SELECT 'Credits Table Structure:' as status;
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'credits' 
ORDER BY ordinal_position;

-- Verify echeances table structure
SELECT 'Echeances Table Structure:' as status;
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'echeances' 
ORDER BY ordinal_position;

-- Verify incidents table structure
SELECT 'Incidents Table Structure:' as status;
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'incidents' 
ORDER BY ordinal_position;
