Imports System.ComponentModel
Imports System.Security.Cryptography
Imports System.Security.Policy
Imports System.Text

Public Class InfoChange
	Private mainForm As MainWindow

	Public Sub New(mainForm As MainWindow)

		' This call is required by the designer.
		InitializeComponent()

		' Add any initialization after the InitializeComponent() call.
		Me.mainForm = mainForm
	End Sub

	Private Sub InfoChange_Closing(sender As Object, e As CancelEventArgs) Handles Me.Closing
		mainForm.Show()
	End Sub

	Private Sub InfoChange_Load(sender As Object, e As EventArgs) Handles MyBase.Load
		txtUsername.Text = SettingManager.getUsername
	End Sub

	Private Sub btnChangeUser_Click(sender As Object, e As EventArgs) Handles btnChangeUser.Click
		Dim username = txtUsername.Text
		If Not username = "" Then
			SettingManager.setUsername(username)
			XChaChaCrypto.loadKey()
			MessageBox.Show("Thay đổi tên người dùng thành công!", "ClipSync", MessageBoxButtons.OK, MessageBoxIcon.Information)
		Else
			MessageBox.Show("Tên người dùng không được bỏ trống!", "ClipSync", MessageBoxButtons.OK, MessageBoxIcon.Error)
		End If
	End Sub

	Private Sub btnChangePass_Click(sender As Object, e As EventArgs) Handles btnChangePass.Click
		Dim currentPass = txtOldPass.Text
		Dim newPass = txtNewPass.Text
		Dim newPassConfirm = txtNewPassConfirm.Text

		If checkPasswordMatch(currentPass) Then
			If newPass = newPassConfirm Then
				If Not newPass = "" Then
					SettingManager.setPassword(getSha512(newPass))
				Else
					SettingManager.setPassword("")
				End If
				XChaChaCrypto.loadKey()
				MessageBox.Show("Thay đổi mật khẩu thành công!", "ClipSync", MessageBoxButtons.OK, MessageBoxIcon.Information)
			Else
				MessageBox.Show("Xác nhận mật khẩu không khớp!", "ClipSync", MessageBoxButtons.OK, MessageBoxIcon.Error)
			End If
		Else
			MessageBox.Show("Sai mật khẩu cũ!", "ClipSync", MessageBoxButtons.OK, MessageBoxIcon.Error)
			txtOldPass.Clear()
		End If
	End Sub

	Private Function getSha512(password As String) As String
		Dim passwordBytes = Encoding.UTF8.GetBytes(password)
		Dim passwordHash As Byte()

		Using sha512 As SHA512 = SHA512.Create()
			passwordHash = sha512.ComputeHash(passwordBytes)
		End Using

		Dim hexString As String = BitConverter.ToString(passwordHash).Replace("-", "").ToLower()

		Return hexString
	End Function

	Private Function checkPasswordMatch(password As String) As Boolean
		Dim currentPass = SettingManager.getPassword()
		If Not currentPass = "" Then
			Return getSha512(password) = currentPass
		Else
			Return currentPass = password
		End If
	End Function
End Class