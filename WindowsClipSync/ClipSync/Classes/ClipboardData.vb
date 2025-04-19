Public Class ClipboardData
	Private Shared latestData As String = ""

	Public Shared Function getLatestData() As String
		Return latestData
	End Function

	Public Shared Sub setLatestData(data As String)
		latestData = data
	End Sub

	Public Shared Sub writeClipboard(text As String)
		setLatestData(text)
		Clipboard.SetText(text)
	End Sub
End Class
