Imports System.Runtime.InteropServices
Imports System.Text

Public Class MainWindow
	Dim broadcastHandler As New BroadcastHandler
	Dim tcpHandler As New TCPHandler

	' Windows API
	Private Const WM_CLIPBOARDUPDATE As Integer = &H31D
	Private Shared ReadOnly HWND_MESSAGE As IntPtr = New IntPtr(-3)

	' Import các hàm API
	<DllImport("user32.dll", SetLastError:=True)>
	Public Shared Function AddClipboardFormatListener(hWnd As IntPtr) As Boolean
	End Function

	<DllImport("user32.dll", SetLastError:=True)>
	Public Shared Function RemoveClipboardFormatListener(hWnd As IntPtr) As Boolean
	End Function

	Protected Overrides Sub WndProc(ByRef m As Message)
		MyBase.WndProc(m)
		If m.Msg = WM_CLIPBOARDUPDATE Then
			' Clipboard changed
			HandleClipboardChange()
		End If
	End Sub

	Private Sub HandleClipboardChange()
		If Clipboard.ContainsText() Then
			Dim clipboardText As String = Clipboard.GetText()

			' TEXT DATA
			' DO SOMETHING HERE
			If ClipboardData.getLatestData() = clipboardText Then
				ClipSyncDebug.log("[MainWindow] Ignored self-triggered clipboard change")
				Return
			End If

			ClipboardData.setLatestData(clipboardText)
			Communication.sendBroadcast()
		ElseIf Clipboard.ContainsImage() Then
			' IMAGE DATA
			' DO SOMETHING HERE
		End If
	End Sub

	Public Sub enableSync(enable As Boolean)
		If enable Then
			btnEnable.Text = "Tắt"
			XChaChaCrypto.loadKey()
			AddClipboardFormatListener(Me.Handle)
			broadcastHandler.startListening()
			tcpHandler.startListening()
		Else
			btnEnable.Text = "Bật"
			RemoveClipboardFormatListener(Me.Handle)
			broadcastHandler.stopListening()
			tcpHandler.stopListening()
		End If
	End Sub

	Private Sub MainWindow_Load(sender As Object, e As EventArgs) Handles MyBase.Load
		Dim notiIcon As NotifyIconManager = New NotifyIconManager(Me)

		enableSync(SettingManager.isEnable())
	End Sub

	Private Sub btnChangeInfo_Click(sender As Object, e As EventArgs) Handles btnChangeInfo.Click
		Dim infoChange As New InfoChange(Me)

		infoChange.Show()
		Me.Hide()
	End Sub

	Private Sub btnEnable_Click(sender As Object, e As EventArgs) Handles btnEnable.Click
		SettingManager.setEnable(Not SettingManager.isEnable)

		enableSync(SettingManager.isEnable())
	End Sub

	Private Sub btnSend_Click(sender As Object, e As EventArgs) Handles btnSend.Click
		ClipboardData.writeClipboard(richData.Text)
		Communication.sendBroadcast()
	End Sub
End Class