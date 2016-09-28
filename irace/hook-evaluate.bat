@echo off

rem ###############################################################################
rem # This hook is run for each candidate to evaluate it after all
rem # candidate configurations have been run on a single instance.
rem #
rem #
rem # PARAMETERS:
rem # $1 is the instance name
rem # $2 is the candidate number (id)
rem # $3 is the total number of candidates alive in this iteration
rem #
rem # RETURN VALUE:
rem # This hook should print one numerical value: the cost that must be minimized.
rem # Exit with 0 if no error, with 1 in case of error
rem ###############################################################################

rem -- The instance name and the candidate id are the first parameters
set INSTANCE=%1
set CANDIDATE=%2
set TOTALCANDIDATES=%3

set HV=c%CANDIDATE%.dat
set LOGS=c%CANDIDATE%.log

rem -- What "fixed" parameters should be always passed to program?
set FIXED_PARAMS=--candidateId %CANDIDATE% --fileName %HV% --directory %CD%

rem -- Now we can call the program by building a command line with all parameters for it
rem copy NUL %HV%
java -d64 -Xmx5g -cp "../../target/*:../../target/lib/*" br.ufpr.inf.gres.irace.core.HookEvaluate %FIXED_PARAMS% > %LOGS% 2>&1

rem -- Read a number from the output of the evaluation result 
set /p cost=<%HV%
echo -%cost%

rem -- Delete the unecessary files
del FUN_%CANDIDATE%
del VAR_%CANDIDATE%
del TIME_FUN_%CANDIDATE%
del %LOGS%

exit %errorlevel%