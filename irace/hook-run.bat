@echo off

rem ###############################################################################
rem # This hook is the command that is executed every run.
rem #
rem # This hook is run in the execution directory (execDir, --exec-dir),
rem # the same directory where hook-evaluate is executed. Hence, you may
rem # need to copy extra files needed by the executable to this directory.
rem #
rem #
rem # PARAMETERS:
rem # $1 is the instance name
rem # $2 is the candidate number (id)
rem # The rest are parameters to the run
rem #
rem # RETURN VALUE:
rem # This hook should print one numerical value: the cost that must be minimized.
rem # Exit with 0 if no error, with 1 in case of error
rem ###############################################################################

rem -- The instance name and the candidate id are the first parameters
set INSTANCE=%1
set CANDIDATE=%2

rem -- What "fixed" parameters should be always passed to program?
set FIXED_PARAMS=--candidateId %CANDIDATE% --directory %CD%

rem -- All other parameters are the candidate parameters to be passed to program
rem #Begin
shift
shift
set CAND_PARAMS=%1

:loop
shift
if [%1]==[] goto afterloop
set CAND_PARAMS=%CAND_PARAMS% %1
goto loop
:afterloop

rem #End

set LOGS=c%CANDIDATE%.log

rem -- Now we can call the program by building a command line with all parameters for it
java -d64 -Xmx5g -cp ".../../target/*;../../target/lib/*" br.ufpr.inf.gres.irace.core.HookRun %FIXED_PARAMS% %CAND_PARAMS% > %LOGS% 2>&1

exit %errorlevel%