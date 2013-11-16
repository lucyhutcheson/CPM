//
//  AddViewController.h
//  testApp
//
//  Created by Lucy Hutcheson on 12/13/12.
//  Copyright (c) 2012 Lucy Hutcheson. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol AddViewDelegate <NSObject>

@required
-(void)onSave:(NSString*)myFirst;

@end

@interface AddViewController : UIViewController <UITextFieldDelegate>
{
    id<AddViewDelegate> delegate;
    IBOutlet UITextField *firstField;
    IBOutlet UITextField *lastField;
    IBOutlet UITextField *emailField;
    IBOutlet UITextField *phoneField;
    IBOutlet UITextField *ageField;
    NSString *myData;
}

@property(strong) id<AddViewDelegate> delegate;
@property(nonatomic, strong) IBOutlet UIButton *saveButton;
@property(nonatomic, strong) IBOutlet UIButton *cancelButton;

-(IBAction)onClick:(id)sender;
- (IBAction)textFieldDoneEditing:(id)sender;

@end
