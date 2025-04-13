Imports System.Threading

Public Class BroadcastHandler
	Private Const PORT = 7070
	Private running = True
	Private broadcastListening As New Thread(AddressOf listening)

	Public Sub start()
		broadcastListening.Start()
	End Sub

	Private Sub listening()

	End Sub
End Class
