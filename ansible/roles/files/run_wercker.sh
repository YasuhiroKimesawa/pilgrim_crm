#!/usr/bin/env bash
curl -X POST -H "Authorization: Bearer $WERCKER_TOKEN" -H 'Content-type: application/json' https://app.wercker.com/api/v3/runs -d '{"pipelineId":$WERCKER_PIPELINE_ID,"branch": "master"}'