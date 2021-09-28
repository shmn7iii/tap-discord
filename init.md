# Install

CentOS 7.9

Java 17 (OpenJDK 17)

1. Download latest jar from GitHub

2. Download credential.json from firebase

4. Set up for systemd

	/etc/systemd/system/tap-discord.service

```
[Unit]
Description=Discord BOT for Tap!

[Service]
User=root
WorkingDirectory=/root/tap-discord
ExecStart=/opt/jdk-17/bin/java -jar ./tap-discord.jar -token <token> -credential ./tap-credential.json

[Install]
WantedBy=multi-user.target
```

4. Enable and start

```
$ sudo systemctl enable tap-discord
$ sudo systemctl start tap-discord
```
