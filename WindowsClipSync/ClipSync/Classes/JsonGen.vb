Imports Newtonsoft.Json.Linq

Public Class JsonGen
	Public Shared Function otpAuthJson(otp As String) As String
		Try
			Dim jsonObject As JObject = New JObject()

			jsonObject.Add("type", "otp_auth")
			jsonObject.Add("value", otp)

			Return jsonObject.ToString()
		Catch ex As Exception
			MessageBox.Show($"Đã xảy ra lỗi: {ex.Message}", "ClipSync", MessageBoxButtons.OK, MessageBoxIcon.Error)
			Return Nothing
		End Try
	End Function

	Public Shared Function dataJson(data As String) As String
		Try
			Dim jsonObject As JObject = New JObject()

			jsonObject.Add("type", "data")
			jsonObject.Add("value", data)

			Return jsonObject.ToString()
		Catch ex As Exception
			MessageBox.Show($"Đã xảy ra lỗi: {ex.Message}", "ClipSync", MessageBoxButtons.OK, MessageBoxIcon.Error)
			Return Nothing
		End Try
	End Function
End Class

