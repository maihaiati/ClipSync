Imports System.Net
Imports System.Net.Sockets
Imports System.Text

Public Class Communication
	Private Const PORT = 7070

	Public Shared Sub sendBroadcast()
		Dim udpClient As New UdpClient()
		udpClient.EnableBroadcast = True

		Dim data = Encoding.UTF8.GetBytes("CS_BC_" + SettingManager.getUsername())
		Dim endpoint As New IPEndPoint(IPAddress.Broadcast, PORT)

		udpClient.Send(data, data.Length, endpoint)
		udpClient.Close()
	End Sub

	Public Shared Sub sendSyncRequest(ip As IPAddress)

	End Sub

	Public Shared Sub sendClipboardData(ip As IPAddress)

	End Sub
End Class
