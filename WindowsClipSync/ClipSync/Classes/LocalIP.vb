Imports System.Net
Imports System.Net.Sockets

Public Class LocalIP
	Public Shared Function getIPAddress() As String
		Dim localIP As String = String.Empty

		Try
			Using socket As New Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp)
				socket.Connect("8.8.8.8", 65530)
				Dim endPoint As IPEndPoint = CType(socket.LocalEndPoint, IPEndPoint)
				localIP = endPoint.Address.ToString()
			End Using
		Catch ex As Exception
			ClipSyncDebug.log("[LocalIP] Lỗi lấy địa chỉ IP: " & ex.Message)
		End Try

		Return localIP
	End Function
End Class
