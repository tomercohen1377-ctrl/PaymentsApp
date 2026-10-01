# Billing test server

Express server from `android-test-server.zip`, serving the billing data the app reads.

## Run

```bash
cd server
npm install
npm run dev        # Express app running on port 8030!
```

From the Android emulator the server is reachable at `http://10.0.2.2:8030/`. For a physical device,
run `adb reverse tcp:8030 tcp:8030` and use `http://localhost:8030/`.

## Endpoints (all `POST`, JSON body)

| Path | Body | Response |
|---|---|---|
| `/payment/billing/entry/headers` | `{}` | `{ "headers": [BillingEntryHeader] }` |
| `/payment/billing/entry/details` | `{ "billingId": 5200 }` | `{ "details": BillingEntryDetails }`, or `{ "details": null }` for an unknown id |
| `/payment/billing/entry/delete` | `{ "billingId": 5200 }` | `{ "status": 0 }` on success, `{ "status": -1 }` if the id doesn't exist |

```bash
curl -s -X POST localhost:8030/payment/billing/entry/delete \
  -H 'Content-Type: application/json' -d '{"billingId":5200}'
```

## Notes

- **Fixed:** delete used `splice(index)` without a delete count, which removed every entry from
  that index to the end of `list.json` / `details.json` (deleting one id left 35 of 486 entries).
  It now removes only the requested entry, and accepts the id as a number or a numeric string.
- Deletes are written to `list.json` / `details.json`. To restore the original data:

  ```bash
  git checkout server/list.json server/details.json
  ```
