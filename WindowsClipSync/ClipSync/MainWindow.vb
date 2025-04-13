Imports System.Runtime.InteropServices
Imports System.Text

Public Class MainWindow
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
			MsgBox(XChaChaCrypto.encrypt(clipboardText, Encoding.UTF8.GetBytes("metadata")))
			MsgBox(clipboardText)

		ElseIf Clipboard.ContainsImage() Then
			' IMAGE DATA
			' DO SOMETHING HERE
		End If
	End Sub

	Private Sub MainWindow_Load(sender As Object, e As EventArgs) Handles MyBase.Load
		Dim notiIcon As NotifyIconManager = New NotifyIconManager(Me)

		XChaChaCrypto.loadKey()
		AddClipboardFormatListener(Me.Handle)
	End Sub

	Private Sub btnChangeInfo_Click(sender As Object, e As EventArgs) Handles btnChangeInfo.Click
		Dim infoChange As New InfoChange(Me)

		infoChange.Show()
		Me.Hide()
	End Sub
End Class