Imports System.Security.Cryptography
Imports System.Text

Public Class Authenticator
	Public Shared Function genOTP() As String
		Dim currentTime As Long = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() \ 30000 ' Every 30 seconds
		Dim timeHex As String = currentTime.ToString("X").ToUpper()
		Try
			' Retrieve the password
			Dim passHash As String = SettingManager.getPassword()

			If String.IsNullOrEmpty(passHash) Then Return ""
			Dim keyBytes As Byte() = Encoding.UTF8.GetBytes(passHash)
			Dim mac As New HMACSHA1(keyBytes)

			Dim hash As Byte() = mac.ComputeHash(Encoding.UTF8.GetBytes(timeHex))

			' Extract 6-digit OTP
			Dim offset As Integer = hash(hash.Length - 1) And &HF
			Dim otp As Integer = ((hash(offset) And &H7F) << 24) Or
							((hash(offset + 1) And &HFF) << 16) Or
							((hash(offset + 2) And &HFF) << 8) Or
							 (hash(offset + 3) And &HFF)
			otp = otp Mod 1000000 ' Keep only the last 6 digits

			Return otp.ToString("D6")
		Catch ex As Exception
			ClipSyncDebug.log(ex.Message)
		End Try
		Return ""
	End Function

	Public Shared Function checkOTPMatch(otp As String) As Boolean
		Return genOTP() = otp
	End Function
End Class
