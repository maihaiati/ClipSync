Public Class ClipSyncDebug
	Private Shared debugEnable = True

	Public Shared Sub log(message As String)
		If debugEnable Then
			Debug.Print(message)
		End If
	End Sub
End Class
