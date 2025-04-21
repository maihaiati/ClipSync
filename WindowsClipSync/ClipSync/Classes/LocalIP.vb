Imports System.Net
Imports System.Net.Sockets

Public Class LocalIP
	Public Shared Function getIPAddress() As IPAddress
		Dim localIP As IPAddress

		Try
			Using socket As New Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp)
				socket.Connect("8.8.8.8", 65530)
				Dim endPoint As IPEndPoint = CType(socket.LocalEndPoint, IPEndPoint)
				localIP = endPoint.Address
			End Using
			Return localIP
		Catch ex As Exception
			ClipSyncDebug.log("[LocalIP] Get IP Address failed: " & ex.Message)
		End Try
		Return Nothing
	End Function
End Class
