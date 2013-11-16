//
//  AddViewController.m
//  testApp
//
//  Created by Lucy Hutcheson on 12/13/12.
//  Copyright (c) 2012 Lucy Hutcheson. All rights reserved.
//

#import "AddViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "ListViewController.h"
#import "DBManager.h"

@interface AddViewController ()

@end

@implementation AddViewController

@synthesize delegate, saveButton, cancelButton;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        self.title = NSLocalizedString(@"Add New", @"Add New");
        
        // empty this out
        delegate = nil;
    }
    return self;
}

- (void)viewDidLoad
{
    // Green Navigation Bar
    self.navigationController.navigationBar.tintColor = [UIColor colorWithRed:103.0/255.0 green:158.0/255.0 blue:8.0/255.0 alpha:1.0];

    // Close Keyboard Button
    UIBarButtonItem *closeButton = [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(@"Close Keyboard", @"") style:UIBarButtonItemStyleBordered  target:self action:@selector(closeKeyboard:)];
    self.navigationItem.rightBarButtonItem = closeButton;

    // Round out custom buttons
    NSArray *buttons = [NSArray arrayWithObjects: self.saveButton, self.cancelButton,nil];
    
    for(UIButton *btn in buttons)
    {
        CALayer *addBtnLayer = [btn layer];
        [addBtnLayer setMasksToBounds:YES];
        [addBtnLayer setCornerRadius:7.0f];
    }

    
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)closeKeyboard:(id)sender
{
    [firstField resignFirstResponder];
    [lastField resignFirstResponder];
    [emailField resignFirstResponder];
    [phoneField resignFirstResponder];
    [ageField resignFirstResponder];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)textFieldDoneEditing:(id)sender
{
    [sender resignFirstResponder];
}


-(IBAction)onClick:(id)sender
{
    UIButton *button = (UIButton *)sender;
    if (button != nil)
    {
        if (button.tag == 0)
        {
            // If the event name is empty, throw up an alert
            if ([firstField.text isEqualToString:@""])
            {
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Oops!" message:@"A name is required." delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                if (alertView != nil)
                {
                    [alertView show];
                }
            }
            else
            {
                NSLog(@"save");
                NSNumber *ageNumber = [NSNumber numberWithInteger:[ageField.text integerValue]];
                [[DBManager getSharedInstance] saveData:firstField.text last:lastField.text email:emailField.text phone:phoneField.text age:ageNumber];
                
                [self.navigationController popToRootViewControllerAnimated:YES];
            }

        }
        else if (button.tag == 1)
        {
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
    }
}


@end
