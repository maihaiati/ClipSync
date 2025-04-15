Imports System.Net
Imports System.Net.Sockets
Imports System.Threading

Public Class BroadcastHandler
	Private Const PORT = 7070
	Private running = True
	Private socket As Socket
	Private broadcastListening As New Thread(AddressOf listening)

	Public Sub startListening()
		broadcastListening.Start()
	End Sub

	Public Sub stopListening()

	End Sub

	Private Function checkSenderMatch(receivedMessage As String) As Boolean
		Dim broadcastContent = "CS_BC_" + SettingManager.getUsername()
		Return receivedMessage = broadcastContent
	End Function

	Private Sub listening()
		socket = New Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp)
		Dim iep = New IPEndPoint(IPAddress.Any, PORT)
		socket.Bind(iep)
		Dim ep As EndPoint = iep

	End Sub
End Class
