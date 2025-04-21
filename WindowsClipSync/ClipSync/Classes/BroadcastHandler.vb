Imports System.Net
Imports System.Net.Sockets
Imports System.Text
Imports System.Threading

Public Class BroadcastHandler
	Private broadcastListening As Thread
	Private listener As UdpClient
	Private running = False
	Private UDP_PORT = 7070

	Public Sub startListening()
		running = True
		listener = New UdpClient(New IPEndPoint(LocalIP.getIPAddress(), UDP_PORT))
		broadcastListening = New Thread(AddressOf listening)
		broadcastListening.IsBackground = True
		broadcastListening.Start()
	End Sub

	Public Sub stopListening()
		running = False
		If listener IsNot Nothing Then listener.Close()
	End Sub

	Private Function checkSenderMatch(receivedMessage As String) As Boolean
		Dim broadcastContent = "CS_BC_" + SettingManager.getUsername()
		Return receivedMessage = broadcastContent
	End Function

	Private Sub listening()
		ClipSyncDebug.log("[BroadcastHandler] Start new thread")

		Dim remoteEP As New IPEndPoint(IPAddress.Any, 0)
		While running
			Try
				Dim data As Byte() = listener.Receive(remoteEP)
				Dim message As String = Encoding.UTF8.GetString(data)

				If checkSenderMatch(message) AndAlso LocalIP.getIPAddress().ToString <> remoteEP.Address.ToString Then
					ClipSyncDebug.log("Received from " & remoteEP.Address.ToString() & ": " & message)
					Communication.sendSyncRequest(remoteEP.Address)
				End If
			Catch ex As Exception
				ClipSyncDebug.log("[BroadcastHandler] UDP receive error: " & ex.Message)
			End Try
		End While
		ClipSyncDebug.log("[BroadcastHandler] Stop thread")
	End Sub
End Class
