# init
```
CentOS 7.9
Java 17 (OpenJDK 17)
```

## Install

1. Download latest jar from GitHub

2. Download credential.json from Firebase

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

```bash
$ sudo systemctl enable tap-discord
$ sudo systemctl start tap-discord
```

## Command line usage
```bash
$ java -jar <tap-discord.jar> -token <token> -credential <tap-credential.json> -createcommand <bool>
```

**-token**: DiscordBOT's token

**-credential**: Firebase's credential json file path

**-createcommand**: Whether to run createCommand at startup