Imports System.IO
Imports System.Net
Imports System.Net.Sockets
Imports System.Security.Cryptography.Xml
Imports System.Text
Imports System.Threading

Public Class Communication
	Private Const UDP_PORT = 7070
	Private Const TCP_PORT = 7071

	Public Shared Sub sendBroadcast()
		Dim ipAddr As IPAddress = LocalIP.getIPAddress()

		Dim udpClient As New UdpClient(New IPEndPoint(ipAddr, 0))
		udpClient.EnableBroadcast = True

		Dim data = Encoding.UTF8.GetBytes("CS_BC_" + SettingManager.getUsername())
		Dim endpoint As New IPEndPoint(IPAddress.Broadcast, UDP_PORT)

		For i = 0 To 2 Step 1
			udpClient.Send(data, data.Length, endpoint)
			ClipSyncDebug.log("Broadcast packet " + (i + 1).ToString + " sent to: " + endpoint.Address.ToString)
			Thread.Sleep(100)
		Next
		udpClient.Close()
	End Sub

	Private Shared Sub sendRequestThread(ipAddr As IPAddress)
		Try
			Using socket As New Socket(SocketType.Stream, ProtocolType.Tcp)
				socket.Connect(ipAddr, TCP_PORT)

				Dim otp As String = JsonGen.otpAuthJson(Authenticator.genOTP())

				If otp IsNot Nothing AndAlso otp <> "" Then
					Dim encryptedOTP As String = XChaChaCrypto.encrypt(otp, Encoding.UTF8.GetBytes("metadata"))

					' Send the encrypted OTP
					Using stream As NetworkStream = New NetworkStream(socket)
						Using writer As StreamWriter = New StreamWriter(stream)
							writer.WriteLine(encryptedOTP)
							writer.Flush()
						End Using
					End Using
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
			If ipAddr Is Nothing Then Return
			Using socket As New Socket(SocketType.Stream, ProtocolType.Tcp)
				socket.Connect(ipAddr, TCP_PORT)

				Dim data As String = JsonGen.dataJson(ClipboardData.getLatestData())

				If data IsNot Nothing Then
					Dim encryptedData = XChaChaCrypto.encrypt(data, Encoding.UTF8.GetBytes("metadata"))

					' Send the encrypted data
					Using stream As NetworkStream = New NetworkStream(socket)
						Using writer As StreamWriter = New StreamWriter(stream)
							writer.WriteLine(encryptedData)
							writer.Flush()
						End Using
					End Using
					ClipSyncDebug.log("Sent data: " + data)
					ClipSyncDebug.log("Sent encrypted data: " + encryptedData)
				Else
					ClipSyncDebug.log("[Communication] Clipboard null. Ignore")
				End If
			End Using
		Catch ex As Exception
			ClipSyncDebug.log("[Communication] Send clipboard data failed: " & ex.Message)
		End Try
	End Sub

	Public Shared Sub sendSyncRequest(ipAddr As IPAddress)
		Dim requestThread = New Thread(Sub()
										   sendRequestThread(ipAddr)
									   End Sub)
		requestThread.IsBackground = True
		requestThread.Start()
	End Sub

	Public Shared Sub sendClipboardData(ipAddr As IPAddress)
		Dim clipboardThread = New Thread(Sub()
											 sendClipboardThread(ipAddr)
										 End Sub)
		clipboardThread.IsBackground = True
		clipboardThread.Start()
	End Sub
End Class
