Imports System.IO
Imports System.Net
Imports System.Net.Sockets
Imports System.Text
Imports System.Threading
Imports Newtonsoft.Json.Linq

Public Class TCPHandler
	Private tcpListening As Thread
	Private tcpListener As TcpListener
	Private running = False
	Private TCP_PORT = 7071

	Public Sub startListening()
		running = True
		tcpListening = New Thread(AddressOf listening)
		tcpListening.IsBackground = True
		tcpListening.Start()
	End Sub

	Public Sub stopListening()
		running = False
		If tcpListener IsNot Nothing Then
			tcpListener.Stop()
		End If
	End Sub

	Private Sub clientHandler(ipAddr As IPAddress, encryptData As String)
		Try
			Dim jsonMessage = XChaChaCrypto.decrypt(encryptData, Encoding.UTF8.GetBytes("metadata"))

			ClipSyncDebug.log("[TCPHandler] Message after decrypt: " + jsonMessage)

			Dim jsonObject = JObject.Parse(jsonMessage)

			Select Case jsonObject.GetValue("type").ToString()
				Case "otp_auth"
					Dim otp As String = jsonObject.GetValue("value").ToString()

					If Authenticator.checkOTPMatch(otp) Then
						Communication.sendClipboardData(ipAddr)
					End If

				Case "data"
					ClipboardData.writeClipboard(jsonObject.GetValue("value").ToString)
			End Select
		Catch ex As Exception
			ClipSyncDebug.log("[TCPHandler] Exception: " + ex.Message)
		End Try
	End Sub

	Private Sub listening()
		Try
			Dim ipAddr As IPAddress = LocalIP.getIPAddress()
			tcpListener = New TcpListener(ipAddr, TCP_PORT)
			tcpListener.Start()

			While running
				Using client As Socket = tcpListener.AcceptSocket
					Dim clientIP As IPAddress = IPAddress.Parse(CType(client.RemoteEndPoint, IPEndPoint).Address.ToString())
					ClipSyncDebug.log("[TCPHandler] Client connected: " + clientIP.ToString)

					' Read data from client
					Using stream As NetworkStream = New NetworkStream(client)
						Using reader As New StreamReader(stream)
							Dim message As String = reader.ReadLine()
							ClipSyncDebug.log("[TCPHandler] Received: " & message)

							' Handler
							clientHandler(clientIP, message)
						End Using
					End Using

					' Close connection
					client.Close()
				End Using
			End While

			If tcpListener IsNot Nothing Then
				tcpListener.Stop()
			End If
		Catch ex As Exception
			ClipSyncDebug.log("[TCPHandler] Exception: " + ex.Message)
		End Try
	End Sub
End Class
