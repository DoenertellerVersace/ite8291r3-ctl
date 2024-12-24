#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <stdint.h>
#include <errno.h>
#include "SwayIPC.h"

#define BUFFER_SIZE 12192
#define MAGIC_STRING "i3-ipc"

// IPC message type constants
#define RUN_COMMAND 0
#define GET_WORKSPACES 1
#define SUBSCRIBE 2
#define GET_OUTPUTS 3
#define GET_TREE 4
#define GET_MARKS 5
#define GET_BAR_CONFIG 6
#define GET_VERSION 7
#define GET_BINDING_MODES 8
#define GET_CONFIG 9
#define GET_INPUTS 100
#define GET_SEATS 101

static int sway_socket_fd = -1;

int send_ipc_message(int type, const char *payload) {
    if (sway_socket_fd == -1) {
        perror("Not connected to sway-ipc");
        return 404;
    }

    uint32_t payload_length = strlen(payload);
    uint32_t message_type = type;

    // Construct the header
    char header[14];
    memcpy(header, MAGIC_STRING, 6);
    memcpy(header + 6, &payload_length, 4);
    memcpy(header + 10, &message_type, 4);

    // Send the header and payload
    if (write(sway_socket_fd, header, sizeof(header)) != sizeof(header)) {
        perror("Failed to send IPC message header");
        return -1;
    }

    if (write(sway_socket_fd, payload, payload_length) != payload_length) {
        perror("Failed to send IPC message payload");
        return -1;
    }

    return 0;
}

char *read_ipc_reply() {
    if (sway_socket_fd == -1) {
        perror("Not connected to sway-ipc");
        return NULL;
    }

    static char buffer[BUFFER_SIZE];

    // Read the header (14 bytes)
    int header_length = 14;
    if (read(sway_socket_fd, buffer, header_length) != header_length) {
        perror("Failed to read IPC reply header");
        return NULL;
    }

    uint32_t payload_length;
    memcpy(&payload_length, buffer + 6, 4);

    // Ensure we don't exceed the buffer size
    if (payload_length >= BUFFER_SIZE) {
        fprintf(stderr, "Payload too large\n");
        return NULL;
    }

    // Read the payload
    if (read(sway_socket_fd, buffer, payload_length) != payload_length) {
        perror("Failed to read IPC reply payload");
        return NULL;
    }

    buffer[payload_length] = '\0';
    return buffer;
}
// Connect to Sway IPC socket
JNIEXPORT void JNICALL Java_SwayIPC_connect(JNIEnv *env, jobject obj, jstring socketPath) {
    const char *path = (*env)->GetStringUTFChars(env, socketPath, NULL);

    struct sockaddr_un addr;
    addr.sun_family = AF_UNIX;
    strncpy(addr.sun_path, path, sizeof(addr.sun_path) - 1);

    sway_socket_fd = socket(AF_UNIX, SOCK_STREAM, 0);
    if (connect(sway_socket_fd, (struct sockaddr*)&addr, sizeof(addr)) == -1) {
        perror("Failed to connect to Sway IPC socket");
        return;
    }

    (*env)->ReleaseStringUTFChars(env, socketPath, path);
}

// Send a command (type 0)
JNIEXPORT jstring JNICALL Java_SwayIPC_sendCommand(JNIEnv *env, jobject obj, jstring command) {
    const char *cmdStr = (*env)->GetStringUTFChars(env, command, NULL);
    send_ipc_message(RUN_COMMAND, cmdStr);
    (*env)->ReleaseStringUTFChars(env, command, cmdStr);

    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get workspaces (type 1)
JNIEXPORT jstring JNICALL Java_SwayIPC_getWorkspaces(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_WORKSPACES, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get outputs (type 3)
JNIEXPORT jstring JNICALL Java_SwayIPC_getOutputs(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_OUTPUTS, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get tree (type 4)
JNIEXPORT jstring JNICALL Java_SwayIPC_getTree(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_TREE, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get marks (type 5)
JNIEXPORT jstring JNICALL Java_SwayIPC_getMarks(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_MARKS, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get bar config (type 6)
//JNIEXPORT jstring JNICALL Java_SwayIPC_getBarConfig(JNIEnv *env, jobject obj, jstring barId) {
//    const char *barIdStr = (*env)->GetStringUTFChars(env, barId, NULL);
//    send_ipc_message(GET_BAR_CONFIG, barIdStr);
//    (*env)->ReleaseStringUTFChars(env, barId, barIdStr);
//
//    char *reply = read_ipc_reply();
//    return (*env)->NewStringUTF(env, reply);
//}

// Get version (type 7)
JNIEXPORT jstring JNICALL Java_SwayIPC_getVersion(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_VERSION, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get binding modes (type 8)
JNIEXPORT jstring JNICALL Java_SwayIPC_getBindingModes(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_BINDING_MODES, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get config (type 9)
JNIEXPORT jstring JNICALL Java_SwayIPC_getConfig(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_CONFIG, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get inputs (type 100)
JNIEXPORT jstring JNICALL Java_SwayIPC_getInputs(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_INPUTS, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

// Get seats (type 101)
JNIEXPORT jstring JNICALL Java_SwayIPC_getSeats(JNIEnv *env, jobject obj) {
    send_ipc_message(GET_SEATS, "");
    char *reply = read_ipc_reply();
    return (*env)->NewStringUTF(env, reply);
}

/// Subscribe to events
JNIEXPORT jboolean JNICALL Java_SwayIPC_lolLol(JNIEnv *env, jobject obj, jobjectArray e) {
    int num_events = (*env)->GetArrayLength(env, e);
    if (num_events <= 0) {
        return JNI_FALSE;
    }

    // Calculate the total payload size
    int payload_size = 2; // for the opening and closing brackets
    for (int i = 0; i < num_events; i++) {
        jstring event = (jstring) (*env)->GetObjectArrayElement(env, e, i);
        const char *eventStr = (*env)->GetStringUTFChars(env, event, NULL);
        if (eventStr == NULL) {
            return JNI_FALSE;
        }
        payload_size += strlen(eventStr) + 3; // for quotes and comma
        (*env)->ReleaseStringUTFChars(env, event, eventStr);
    }

    char *payload = (char *)malloc(payload_size);
    if (payload == NULL) {
        perror("Failed to allocate memory for payload");
        return JNI_FALSE;
    }

    char *ptr = payload;
    *ptr++ = '[';
    for (int i = 0; i < num_events; i++) {
        jstring event = (jstring) (*env)->GetObjectArrayElement(env, e, i);
        const char *eventStr = (*env)->GetStringUTFChars(env, event, NULL);
        if (eventStr == NULL) {
            free(payload);
            return JNI_FALSE;
        }
        if (i > 0) {
            *ptr++ = ',';
        }
        *ptr++ = '"';
        strcpy(ptr, eventStr);
        ptr += strlen(eventStr);
        *ptr++ = '"';
        (*env)->ReleaseStringUTFChars(env, event, eventStr);
    }
    *ptr++ = ']';
    *ptr = '\0';

    int result = send_ipc_message(SUBSCRIBE, payload);
    free(payload);
    printf("subscribe function called\n");
    fflush(stdout);  // Ensure the output is flushed immediately
    return result == 0 ? JNI_TRUE : JNI_FALSE;
}

// Close the connection
JNIEXPORT int JNICALL Java_SwayIPC_closeConnection(JNIEnv *env, jobject obj) {
    if (sway_socket_fd != -1) {
        close(sway_socket_fd);
        sway_socket_fd = -1;
        return 0;
    }
    return -1;
}
