Imports System.Security.Cryptography
Imports System.Text
Imports Sodium

Public Class XChaChaCrypto
	Private Shared pbkdf2Key As Byte()

	Public Shared Sub loadKey()
		pbkdf2Key = PBKDF2.deriveKey()
	End Sub

	Private Shared Function CreateNonce(length As Integer) As Byte()
		Dim nonce(length - 1) As Byte
		RandomNumberGenerator.Fill(nonce)
		Return nonce
	End Function

	Public Shared Function encrypt(plaintext As String, associatedData As Byte()) As String
		Dim plainBytes As Byte() = Encoding.UTF8.GetBytes(plaintext)
		Dim nonce = CreateNonce(24) ' XChaCha20 yêu cầu nonce dài 24 bytes

		Dim cipherBytes = SecretAeadXChaCha20Poly1305.Encrypt(plainBytes, nonce, pbkdf2Key, associatedData)
		Dim finalOutput = nonce.Concat(cipherBytes).ToArray()

		Return Convert.ToBase64String(finalOutput)
	End Function

	Public Shared Function decrypt(ciphertext As String, associatedData As Byte()) As String
		Dim cipherBytes = Convert.FromBase64String(ciphertext)

		' Trích xuất nonce và ciphertext
		Dim extractedNonce = cipherBytes.Take(24).ToArray()
		Dim actualCipher = cipherBytes.Skip(24).ToArray()

		Dim decryptedBytes = SecretAeadXChaCha20Poly1305.Decrypt(actualCipher, extractedNonce, pbkdf2Key, associatedData)

		Return Encoding.UTF8.GetString(decryptedBytes)
	End Function
End Class
