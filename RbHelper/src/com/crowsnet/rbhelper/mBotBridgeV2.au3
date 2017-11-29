;read passed args
$char = $CmdLine[1]

;obtain mBot window
$hwnd = ListWin($char, "mBot")
If Not $hwnd Then
   ConsoleWrite("err_no_bot" & @CRLF)
   Exit
EndIf

ConsoleWrite("bridge_bot_detected" & @CRLF)

;the main loop: launch client if not launched, wait till character is 110, log off, exit this script
Wait110()

;cmd_wait_110 function
Func Wait110()
   ;launch client in case it is not launched yet
   LaunchClient()

   ;wait till character is lvl 110
   $is110 = (ControlGetText($hwnd,"","Static33") == "Level: 110")
   While Not $is110
	  $is110 = (ControlGetText($hwnd,"","Static33") == "Level: 110")
	  Sleep(1000)
   WEnd

   ConsoleWrite("bridge_reached_110" & @CRLF)

   ;try to log off and wait for confirmation window
   While Not WinExists("[REGEXPTITLE:(?i)(.*" & "Confirmation" & ".*)]")
	  $success = ControlClick($hwnd,"","Button258")
	  Sleep(1000)
   WEnd

   ;clear all confirmation windows
   Local $list = WinList("[REGEXPTITLE:(?i)(.*" & "Confirmation" & ".*)]")
   For $i = 1 To $list[0][0]
	  WinActivate($list[$i][1])
	  WinWaitActive($list[$i][1])
	  ControlClick($list[$i][1],"","Button1")
   Next

   ConsoleWrite("bridge_kill_confirmed" & @CRLF)

   ;wait until client is killed
   $isKilled = StringInStr(ControlGetText($hwnd, "", "Static38"),"/ 1")
   While Not $isKilled
	  $isKilled = StringInStr(ControlGetText($hwnd, "", "Static38"),"/ 1")
	  Sleep(1000)
   WEnd

   ;notify client is killed
   ConsoleWrite("ok_kill")
   Exit
EndFunc

;function to launch client if not launched already
Func LaunchClient()
   ;check if sro_client is launched already
   $isLaunched = Not StringInStr(ControlGetText($hwnd, "", "Static38"),"/ 1")

   ;if not launched, try to launch and wait for login to finish
   If Not $isLaunched Then
	  ;initiate launch
	  $success = ControlClick($hwnd,"","Button255")

	  ;wait till logged in
	  While Not $isLaunched
		 $isLaunched = Not StringInStr(ControlGetText($hwnd, "", "Static38"),"/ 1")
		 Sleep(1000)
	  WEnd
   EndIf

   ConsoleWrite("bridge_client_launched" & @CRLF)
EndFunc

;obtain first window that contains given $var[, $sub] in title
Func ListWin($var, $sub)
   Local $list = WinList("[REGEXPTITLE:(?i)(.*" & $var & ".*)]")

   For $i = 1 To $list[0][0]
	  If StringInStr($list[$i][0],$sub) Or $sub == Null Then
		 #cs MsgBox($MB_SYSTEMMODAL, "", "Title:" & $list[$i][0])
		 #ce
		 Return $list[$i][1]
	  EndIf
   Next
EndFunc
