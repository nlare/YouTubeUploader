#!/bin/bash
jar -cp lib cvfm App.jar Manifest.txt -C .build/class/ . lib fonts icons
