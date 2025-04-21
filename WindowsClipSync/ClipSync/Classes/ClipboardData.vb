Imports System.Threading

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

		Dim staThread As New Thread(Sub()
										Clipboard.SetText(text)
									End Sub)
		staThread.SetApartmentState(ApartmentState.STA)
		staThread.Start()
		staThread.Join()
	End Sub
End Class
