/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, {useEffect, useState} from 'react';
import type {PropsWithChildren} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Button,
  Alert,
  NativeEventEmitter,
  NativeModules,
  TextInput,
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import Dialog from 'react-native-dialog';

const {SmartCAModule} = NativeModules;

// const onPress = () => {
//   SmartCAModule.createCalendarEvent('testName', 'testLocation');
// };

type SectionProps = PropsWithChildren<{
  title: string;
}>;

function Section({children, title}: SectionProps): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View style={styles.sectionContainer}>
      <Text
        style={[
          styles.sectionTitle,
          {
            color: isDarkMode ? Colors.white : Colors.black,
          },
        ]}>
        {title}
      </Text>
      <Text
        style={[
          styles.sectionDescription,
          {
            color: isDarkMode ? Colors.light : Colors.dark,
          },
        ]}>
        {children}
      </Text>
    </View>
  );
}

function App() {
  const [text, setText] = useState('');
  const [code, setCode] = useState('');
  const [visible, setVisible] = useState(false);
  const [valueInput, onChangeText] = React.useState('');

  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const moduleCallback = (code, tokenOrStatusCode, certIdOrStatusDesc) => {
    console.log(code);
    console.log(tokenOrStatusCode);
    console.log(certIdOrStatusDesc);

    // let message = `
    //   ${code === 0 ? 'token' : 'status code'}: ${tokenOrStatusCode}
    //   ${code === 0 ? 'certId' : 'status desc'}: ${certIdOrStatusDesc}
    // `;

    // Alert.alert(code == 1 ? 'Error' : 'Success', message, [
    //   {text: 'OK', onPress: () => console.log('OK Pressed')},
    // ]);

    setText(certIdOrStatusDesc);

    // Alert.alert('test', tokenOrStatusCode);
  };

  const onPress2 = function () {
    // CalendarModule.createCalendarEvent('testName', 'testLocation');
    // CalendarModule.increment();
    // console.log(CalendarModule);

    setVisible(false);
    SmartCAModule.getAuth();

    // Alert.alert('test', 'message');
  };

  const onPress3 = function () {
    setVisible(false);
    SmartCAModule.getMainInfo();

    // Alert.alert('test', 'message');
  };

  const onPress4 = function () {
    setVisible(false);
    console.log(valueInput);
    SmartCAModule.getWaitingTransaction("", valueInput);

    // Alert.alert('test', 'message');
  };

  const onPress5 = function () {
    setVisible(false)
    SmartCAModule.createAccount()
  }

  const onPress6 = function () {
    setVisible(false)
    SmartCAModule.signOut()
  }

  useEffect(() => {
    const eventEmitter = new NativeEventEmitter(SmartCAModule);
    let eventListener = eventEmitter.addListener('EventReminder', event => {
      // console.log(event.eventProperty);
      let code = event.code;
      let tokenOrStatusCode = event.token;
      let certIdOrStatusDesc = event.credentialId;
      // console.log(code);
      // console.log(tokenOrStatusCode);
      // console.log(certIdOrStatusDesc);

      let message = `
    ${code === 0 ? 'token' : 'status code'}: ${tokenOrStatusCode}
    ${code === 0 ? 'certId' : 'status desc'}: ${certIdOrStatusDesc}
  `;

      console.log(message);

      setCode(code === 0 ? 'success' : 'error');

      setText(message);

      setVisible(true);

      // console.log(visible)

      Alert.alert(code == 1 ? 'Error' : 'Success', message, [
        {text: 'OK', onPress: () => console.log('OK Pressed')},
      ]);

      // Alert.alert('test', tokenOrStatusCode);
    });

    let testIOSEvent = eventEmitter.addListener('onIncrement', event => {
      console.log('onIncrement event', event);
    });

    // Removes the listener once unmounted
    return () => {
      eventListener.remove();
      testIOSEvent.remove();
    };
  }, []);

  const showDialog = () => {
    setVisible(true);
  };

  const handleCancel = () => {
    setVisible(false);
  };

  // const handleDelete = () => {
  //   // The user has pressed the "Delete" button, so here you can do your own logic.
  //   // ...Your logic
  //   setVisible(false);
  // };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        {/* <AppBar></AppBar> */}
        {/* <Header /> */}
        {/* <Button
          onPress={onPress}
          title="Learn More"
          color="#841584"
          accessibilityLabel="Learn more about this purple button"
        /> */}

        <View style={styles.button}>
          <Button
            onPress={onPress5}
            title="Create Account"
            color="#841584"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
        <View style={styles.button}>
          <Button
            onPress={onPress2}
            title="Get Auth"
            color="#841584"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
        <Button
          onPress={onPress3}
          title="Get Main"
          color="#841584"
          accessibilityLabel="Learn more about this purple button"
        />
        <TextInput
          style={styles.input}
          onChangeText={onChangeText}
          value={valueInput}
          placeholder="Transaction ID"
        />
        <View style={styles.button}>
          <Button
            onPress={onPress4}
            title="Get Waiting Transaction"
            color="#841584"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
        <View style={styles.button}>
          <Button
            onPress={onPress6}
            title="SignOut"
            color="#841584"
            accessibilityLabel="Learn more about this purple button"
          />
        </View>
      
        {/* <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
          <Section title="Step 1">
            Edit <Text style={styles.highlight}>""</Text> to change this screen
            and then come back to see your edits.
          </Section>
          <Section title="See Your Changes">
            <ReloadInstructions />
          </Section>
          <Section title="Debug">
            <DebugInstructions />
          </Section>
          <Section title="Learn More">
            Read the docs to discover what to do next:
          </Section>
          <LearnMoreLinks />
        </View> */}
      </ScrollView>
      {/* <View>
        <Dialog.Container
          visible={visible}
          verticalButtons={true}
          onRequestClose={handleCancel}>
          <Dialog.Title>{code}</Dialog.Title>
          <ScrollView>
            <Dialog.Description>{text}</Dialog.Description>
          </ScrollView>
          <Dialog.Button label="OK" onPress={handleCancel} />
        </Dialog.Container>
      </View> */}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
  input: {
    height: 40,
    margin: 12,
    borderWidth: 1,
    padding: 10,
  },
  button: {
    marginBottom: 12,
  },
});

export default App;
