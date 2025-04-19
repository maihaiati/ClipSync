Imports System.Net
Imports System.Net.Sockets
Imports System.Text
Imports System.Threading

Public Class Communication
	Private Const UDP_PORT = 7070
	Private Const TCP_PORT = 7071

	Public Shared Sub sendBroadcast()
		Dim ipAddr As IPAddress = IPAddress.Parse(LocalIP.getIPAddress())

		Dim udpClient As New UdpClient(New IPEndPoint(ipAddr, 0))
		udpClient.EnableBroadcast = True

		Dim data = Encoding.UTF8.GetBytes("CS_BC_" + SettingManager.getUsername())
		Dim endpoint As New IPEndPoint(IPAddress.Broadcast, UDP_PORT)

		udpClient.Send(data, data.Length, endpoint)
		udpClient.Close()
	End Sub

	Private Shared Sub sendRequestThread(ipAddr As IPAddress)
		Try
			Using socket As New Socket(SocketType.Stream, ProtocolType.Tcp)
				socket.Connect(ipAddr, TCP_PORT)

				Dim otp As String = JsonGen.otpAuthJson(Authenticator.genOTP())

				If otp IsNot Nothing AndAlso otp <> vbEmpty Then
					Dim encryptedOTP As String = XChaChaCrypto.encrypt(otp, Encoding.UTF8.GetBytes("metadata"))

					' Send the encrypted OTP
					socket.Send(Encoding.UTF8.GetBytes(encryptedOTP))
					ClipSyncDebug.log("[Communication] Sent OTP: " & otp)
					ClipSyncDebug.log("[Communication] Encrypted OTP: " & encryptedOTP)
				Else
					socket.Send(Encoding.UTF8.GetBytes(""))
					ClipSyncDebug.log("[Communication] Send sync request")
				End If
			End Using
		Catch ex As Exception
			ClipSyncDebug.log("[Communication] Sync request failed: " & ex.Message)
		End Try
	End Sub

	Private Shared Sub sendClipboardThread(ipAddr As IPAddress)
		Try
			Using socket As New Socket(SocketType.Stream, ProtocolType.Tcp)
				socket.Connect(ipAddr, TCP_PORT)
			End Using
		Catch ex As Exception
			ClipSyncDebug.log("[Communication] Send clipboard data failed: " & ex.Message)
		End Try
	End Sub

	Public Shared Sub sendSyncRequest(ip As IPAddress)
		Dim requestThread = New Thread(AddressOf sendRequestThread)
		requestThread.IsBackground = True
		requestThread.Start()
	End Sub

	Public Shared Sub sendClipboardData(ip As IPAddress)
		Dim clipboardThread = New Thread(AddressOf sendClipboardThread)
		clipboardThread.IsBackground = True
		clipboardThread.Start()
	End Sub
End Class
