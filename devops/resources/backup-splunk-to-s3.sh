#!/bin/bash

cd $WORKSPACE/configuration
pip install -r requirements.txt

cd $WORKSPACE/configuration/playbooks/edx-east

ansible-playbook -u ubuntu -i $host_ip,  splunk_config_backup.yml -e "splunk_host_id=$splunk_host \
      splunk_s3_backups_bucket=$s3_bucket"
