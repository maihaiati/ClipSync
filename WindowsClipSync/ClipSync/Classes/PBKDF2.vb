Imports System.Security.Cryptography
Imports System.Text

Public Class PBKDF2
	Private Const ITERATIONS = 100000
	Private Const KEY_LENGTH = 32 ' 32 bytes = 256 bit

	Public Shared Function deriveKey() As Byte()
		Try
			Dim pass = SettingManager.getPassword()
			Dim salt = Encoding.UTF8.GetBytes(SettingManager.getUsername)

			Dim pbkdf2 As New Rfc2898DeriveBytes(pass, salt, ITERATIONS)
			Return pbkdf2.GetBytes(KEY_LENGTH)
		Catch ex As Exception
			MessageBox.Show($"Đã xảy ra lỗi: {ex.Message}", "ClipSync", MessageBoxButtons.OK, MessageBoxIcon.Error)
			Return Nothing
		End Try
	End Function
End Class
