pragma solidity ^0.4.16;

contract owned {
    address public owner;

    function owned() public {
        owner = msg.sender;
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }

    function transferOwnership(address newOwner) onlyOwner public {
        owner = newOwner;
    }
}

interface tokenRecipient { function receiveApproval(address _from, uint256 _value, address _token, bytes _extraData) public; }

contract TokenERC20 {
    // Public variables of the token
    string public name;
    string public symbol;
    uint8 public decimals = 0;
    // 18 decimals is the strongly suggested default, avoid changing it
    uint256 public totalSupply;

    // This creates an array with all balances
    mapping (address => uint256) public balanceOf;
    mapping (address => mapping (address => uint256)) public allowance;

    // This generates a public event on the blockchain that will notify clients
    event Transfer(address indexed from, address indexed to, uint256 value);

    // This notifies clients about the amount burnt
    event Burn(address indexed from, uint256 value);

    /**
     * Constrctor function
     *
     * Initializes contract with initial supply tokens to the creator of the contract
     */
    function TokenERC20(
        uint256 initialSupply,
        string tokenName,
        string tokenSymbol
    ) public {
        totalSupply = initialSupply * 10 ** uint256(decimals);  // Update total supply with the decimal amount
        balanceOf[msg.sender] = totalSupply;                // Give the creator all initial tokens
        name = tokenName;                                   // Set the name for display purposes
        symbol = tokenSymbol;                               // Set the symbol for display purposes
    }

    /**
     * Internal transfer, only can be called by this contract
     */
    function _transfer(address _from, address _to, uint _value) internal returns (bool){
        // Prevent transfer to 0x0 address. Use burn() instead
        require(_to != 0x0);
        // Check if the sender has enough
        require(balanceOf[_from] >= _value);
        // Check for overflows
        require(balanceOf[_to] + _value > balanceOf[_to]);
        // Save this for an assertion in the future
        uint previousBalances = balanceOf[_from] + balanceOf[_to];
        // Subtract from the sender
        balanceOf[_from] -= _value;
        // Add the same to the recipient
        balanceOf[_to] += _value;
        Transfer(_from, _to, _value);
        // Asserts are used to use static analysis to find bugs in your code. They should never fail
        assert(balanceOf[_from] + balanceOf[_to] == previousBalances);
    }

    /**
     * Transfer tokens
     *
     * Send `_value` tokens to `_to` from your account
     *
     * @param _to The address of the recipient
     * @param _value the amount to send
     * Used by client to transfer humans
     * quick mafs, 1 human coin, 1 / 95 chance of being lost. 2 human coins, 1 / 90, multiplication of 5 etc.
     */
    function transfer(address _to, uint256 _value) public returns(bool) {
    _transfer(msg.sender, _to, _value);
    }

    /**
     * Transfer tokens from other address
     *
     * Send `_value` tokens to `_to` in behalf of `_from`
     *
     * @param _from The address of the sender
     * @param _to The address of the recipient
     * @param _value the amount to send
     */
    function transferFrom(address _from, address _to, uint256 _value) public returns (bool success) {
        require(_value <= allowance[_from][msg.sender]);     // Check allowance
        allowance[_from][msg.sender] -= _value;
        _transfer(_from, _to, _value);
        return true;
    }

    /**
     * Set allowance for other address
     *
     * Allows `_spender` to spend no more than `_value` tokens in your behalf
     *
     * @param _spender The address authorized to spend
     * @param _value the max amount they can spend
     */
    function approve(address _spender, uint256 _value) public
        returns (bool success) {
        allowance[msg.sender][_spender] = _value;
        return true;
    }

    /**
     * Set allowance for other address and notify
     *
     * Allows `_spender` to spend no more than `_value` tokens in your behalf, and then ping the contract about it
     *
     * @param _spender The address authorized to spend
     * @param _value the max amount they can spend
     * @param _extraData some extra information to send to the approved contract
     */
    function approveAndCall(address _spender, uint256 _value, bytes _extraData)
        public
        returns (bool success) {
        tokenRecipient spender = tokenRecipient(_spender);
        if (approve(_spender, _value)) {
            spender.receiveApproval(msg.sender, _value, this, _extraData);
            return true;
        }
    }

    /**
     * Destroy tokens
     *
     * Remove `_value` tokens from the system irreversibly
     *
     * @param _value the amount of money to burn
     */
    function burn(uint256 _value) public returns (bool success) {
        require(balanceOf[msg.sender] >= _value);   // Check if the sender has enough
        balanceOf[msg.sender] -= _value;            // Subtract from the sender
        totalSupply -= _value;                      // Updates totalSupply
        Burn(msg.sender, _value);
        return true;
    }

    /**
     * Destroy tokens from other account
     *
     * Remove `_value` tokens from the system irreversibly on behalf of `_from`.
     *
     * @param _from the address of the sender
     * @param _value the amount of money to burn
     */
    function burnFrom(address _from, uint256 _value) public returns (bool success) {
        require(balanceOf[_from] >= _value);                // Check if the targeted balance is enough
        require(_value <= allowance[_from][msg.sender]);    // Check allowance
        balanceOf[_from] -= _value;                         // Subtract from the targeted balance
        allowance[_from][msg.sender] -= _value;             // Subtract from the sender's allowance
        totalSupply -= _value;                              // Update totalSupply
        Burn(_from, _value);
        return true;
    }
}

/******************************************/
/*       ADVANCED TOKEN STARTS HERE       */
/******************************************/

contract MyAdvancedToken is owned, TokenERC20 {

    bool public purchaseable = true;
    uint256 public maxSupply = 2000;
    using SafeMath for uint256;

    mapping (address => bool) public frozenAccount;

    /* This generates a public event on the blockchain that will notify clients */
    event FrozenFunds(address target, bool frozen);

    /* Initializes contract with initial supply tokens to the creator of the contract */
    function MyAdvancedToken(
        uint256 initialSupply,
        string tokenName,
        string tokenSymbol
    ) TokenERC20(initialSupply, tokenName, tokenSymbol) public {}

    /* Internal transfer, only can be called by this contract */
    function _transfer(address _from, address _to, uint _value) internal returns (bool) {
        require (_to != 0x0);                               // Prevent transfer to 0x0 address. Use burn() instead
        require (balanceOf[_from] >= _value);               // Check if the sender has enough
        require (balanceOf[_to] + _value > balanceOf[_to]); // Check for overflows
        balanceOf[_from] -= _value;                         // Subtract from the sender
        balanceOf[_to] += _value;                           // Add the same to the recipient
        Transfer(_from, _to, _value);
        return true;
    }

    function enslaveTest(uint times) payable public returns (bool){
      for(uint a = 0; a < times; a++){
        enslaveCustom(a);
      }
      return true;
    }

    function sendHumanTest(uint times, address _to, uint _value) public returns (bool){
      for(uint a = 0; a < times; a++){
        sendHumanCustom(_to,_value,a);
      }
      return true;
    }

    function sendHuman(address _to, uint _value) public returns (bool){
      address _from = msg.sender;
      require (_to != 0x0);                               // Prevent transfer to 0x0 address. Use burn() instead
      require (balanceOf[_from] >= _value);               // Check if the sender has enough
      require (balanceOf[_to] + _value > balanceOf[_to]); // Check for overflows

      uint randomNumber = uint(block.blockhash(block.number))%(9+_value) + 1; //Continuing with spirit of humans coin's absolete nature, humanpool
                                                                                    //coins can be lost during transfering. For each humancoin
                                                                                    //transfered in one bundle, chance of humans disappearing greatly increases
      if( randomNumber< _value){
        balanceOf[_from] = balanceOf[_from].sub(_value);                         // Subtract from the sender                        // Add the same to the recipient
        Burn(_from, _value);
        return true;
      }
      balanceOf[_from] = balanceOf[_from].sub(_value);                         // Subtract from the sender
      balanceOf[_to] = balanceOf[_to].add(_value);                           // Add the same to the recipient
      Transfer(_from, _to, _value);
      return true;
    }

    function sendHumanCustom(address _to, uint _value, uint loop) public returns (bool){
      address _from = msg.sender;
      require (_to != 0x0);                               // Prevent transfer to 0x0 address. Use burn() instead
      require (balanceOf[_from] >= _value);               // Check if the sender has enough
      require (balanceOf[_to] + _value > balanceOf[_to]); // Check for overflows

      uint randomNumber = uint(block.blockhash(block.number-loop))%(10) + 1; //Continuing with spirit of humans coin's absolete nature, humanpool
                                                                                    //coins can be lost during transfering. For each humancoin
                                                                                    //transfered in one bundle, chance of humans disappearing greatly increases
      if(randomNumber<5){
        balanceOf[_from] -= _value;                         // Subtract from the sender                        // Add the same to the recipient
        Burn(_from, _value);
        return true;
      }
      balanceOf[_from] -= _value;                         // Subtract from the sender
      balanceOf[_to] += _value;                           // Add the same to the recipient
      Transfer(_from, _to, _value);
      return true;
    }


    /// @notice Buy from gui
    function enslave() payable public returns (bool){
      if(!purchaseable){
        return false;
      }
      address target = msg.sender;
      uint randomNumber = uint(block.blockhash(block.number))%100 + 1;
      totalSupply = totalSupply.add(1);                                    //regardless if you lose your human,
                                                          //the human will be removed from the humanpool

      if(totalSupply >= maxSupply)
        purchaseable = false;

      uint addedAmount = 1;

      if(randomNumber==66){                              //1 in 100 chance your contract is thrown away essentially
        randomNumber = uint((block.blockhash(block.number-1)) )%100 + 1; //waste your gas for trying to buy a human
        Transfer(0, this, addedAmount);
        Burn(this,addedAmount);                                          //broadcast burned token to network
        return true;                                                    //no human for you contract is dead.
      }

      balanceOf[target] = balanceOf[target].add(addedAmount);
      Transfer(0, this, addedAmount);
      Transfer(this, target, addedAmount);
      return true;
    }

    function enslaveCustom(uint custom) payable public returns (bool){

      if(!purchaseable){
        return false;
      }
      address target = msg.sender;
      uint randomNumber = uint(block.blockhash(block.number-custom))%100 + 1;
      totalSupply = totalSupply.add(1);                                    //regardless if you lose your human,
                                                          //the human will be removed from the humanpool

      if(totalSupply >= maxSupply)
        purchaseable = false;

      uint addedAmount = 1;

      if(randomNumber==100){                              //1 in 100 chance your contract is thrown away essentially
        randomNumber = uint((block.blockhash(block.number-1)) )%100 + 1; //waste your gas for trying to buy a human
        Transfer(0, this, addedAmount);
        Burn(this,addedAmount);                                          //broadcast burned token to network
        return true;                                                    //no human for you contract is dead.
      }

      balanceOf[target] = balanceOf[target].add(addedAmount);
      Transfer(0, this, addedAmount);
      Transfer(this, target, addedAmount);
      return true;
    }

}



//Practice Safe Math Kids!//


/**
 * @title SafeMath
 * @dev Math operations with safety checks that throw on error
 */
library SafeMath {

  /**
  * @dev Multiplies two numbers, throws on overflow.
  */
  function mul(uint256 a, uint256 b) internal pure returns (uint256) {
    if (a == 0) {
      return 0;
    }
    uint256 c = a * b;
    assert(c / a == b);
    return c;
  }

  /**
  * @dev Integer division of two numbers, truncating the quotient.
  */
  function div(uint256 a, uint256 b) internal pure returns (uint256) {
    // assert(b > 0); // Solidity automatically throws when dividing by 0
    uint256 c = a / b;
    // assert(a == b * c + a % b); // There is no case in which this doesn't hold
    return c;
  }

  /**
  * @dev Substracts two numbers, throws on overflow (i.e. if subtrahend is greater than minuend).
  */
  function sub(uint256 a, uint256 b) internal pure returns (uint256) {
    //assert(b <= a);
    //No assert for subtraction, need to be able to do unsigned integer overflow exploit to enslave humanity
    return a - b;
  }

  /**
  * @dev Adds two numbers, throws on overflow.
  */
  function add(uint256 a, uint256 b) internal pure returns (uint256) {
    uint256 c = a + b;
    assert(c >= a);
    return c;
  }
}
