Public Class SettingManager
	' Setters
	Public Shared Sub setUsername(username As String)
		My.Settings.username = username
		My.Settings.Save()
	End Sub

	Public Shared Sub setPassword(password As String)
		My.Settings.password = password
		My.Settings.Save()
	End Sub

	Public Shared Sub setEnable(enable As Boolean)
		My.Settings.enable_sync = enable
		My.Settings.Save()
	End Sub

	' Getters
	Public Shared Function getUsername() As String
		Return My.Settings.username
	End Function

	Public Shared Function getPassword() As String
		Return My.Settings.password
	End Function

	Public Shared Function isEnable() As Boolean
		Return My.Settings.enable_sync
	End Function
End Class
